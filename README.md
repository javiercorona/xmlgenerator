\#!/usr/bin/env python3
"""
Android Project Health Checker CLI

Usage:
scan\_and\_stub.py \[--lint] \[--scan] \[--stubs] \[--html-report REPORT.html]
\[--list-resources] \[--print-refs] \[--html-resources OUT.html]
\[--check-syntax] \[--assemble]

Features:

* \--lint               : Run Gradle lint and report missing resources
* \--scan               : Scan source and res folders for missing resources
* \--stubs              : Auto-generate stub resource files for missing entries
* \--html-report OUT    : Emit an HTML summary including missing resources, syntax errors, and XML errors
* \--list-resources     : Print all defined resources by type
* \--print-refs         : Print all resource references found in code
* \--html-resources OUT : Save all defined resources as an HTML file
* \--check-syntax       : Compile Kotlin sources to catch syntax errors (console)
* \--assemble           : Run Gradle assembleDebug to build the APK
  """

import argparse
import subprocess
import xml.etree.ElementTree as ET
import re
from pathlib import Path
import sys
import shutil

# ================= CONFIGURATION =================

project\_dir = Path(**file**).parent.resolve() / 'MiniOS'
app\_dir     = project\_dir / 'app'
res\_dir     = app\_dir / 'src' / 'main' / 'res'

# Gradle wrapper path

GRADLEW = str(project\_dir / ('gradlew\.bat' if sys.platform.startswith('win') else 'gradlew'))

# ================ RESOURCE UTILITIES ================

def collect\_defined\_resources():
defs = {k: set() for k in \['drawable','layout','mipmap','id','dimen','string','color']}
for res in res\_dir.iterdir():
typ = res.name.split('-')\[0]
if typ in \['drawable','layout','mipmap']:
for f in res.iterdir():
defs\[typ].add(f.stem)
elif typ == 'values':
combined = ''.join(p.read\_text() for p in res.glob('\*.xml'))
patterns = {
'dimen':   r'\<dimen name="(\[^"]+)"',
'string':  r'\<string name="(\[^"]+)"',
'color':   r'\<color name="(\[^"]+)"',
'id':      r'\<item.+type="id".+name="(\[^"]+)"'
}
for key, rgx in patterns.items():
for name in re.findall(rgx, combined):
defs\[key].add(name)
return defs

pattern\_code\_ref = re.compile(r'R.(?P<type>id|drawable|layout|dimen|string|color)/(?P<name>\[A-Za-z0-9\_]+)')

def collect\_code\_references():
refs = {k: set() for k in \['id','drawable','layout','dimen','string','color']}
for f in project\_dir.rglob('\*.\[kj]t'):
text = f.read\_text(errors='ignore')
for m in pattern\_code\_ref.finditer(text):
refs\[m.group('type')].add(m.group('name'))
return refs

# ================ SYNTAX UTILITIES ================

def collect\_syntax\_errors():
proc = subprocess.run(
\[GRADLEW, 'compileDebugKotlin'],
cwd=project\_dir,
stdout=subprocess.PIPE,
stderr=subprocess.PIPE,
text=True
)
errors = \[]
pattern = re.compile(r"(.+.kt):$(\d+),\d+$: (.+)")
for line in proc.stderr.splitlines() + proc.stdout.splitlines():
m = pattern.match(line)
if m:
file, line\_no, msg = m.groups()
errors.append((file, int(line\_no), msg))
return errors

# ================ XML VALIDATION ================

def collect\_xml\_errors():
errors = \[]
for xml\_file in res\_dir.rglob('\*.xml'):
try:
ET.parse(xml\_file)
except ET.ParseError as e:
errors.append((xml\_file.relative\_to(project\_dir), str(e)))
return errors

# ================ FEATURE IMPLEMENTATIONS ================

def run\_gradle\_lint():
print("Running Android Lint...")
subprocess.run(\[GRADLEW, 'lint'], cwd=project\_dir)
report = project\_dir / 'app' / 'build' / 'reports' / 'lint-results.xml'
missing = {}
if report.exists():
tree = ET.parse(report)
for issue in tree.getroot().findall('issue'):
if issue.get('id') == 'MissingResource':
msg = issue.find('message').text or ''
m = re.match(r"@(?P<type>\w+)/(?P<name>\[\w\_]+)", msg)
if m:
missing.setdefault(m.group('type'), set()).add(m.group('name'))
print(f"Lint found missing: {missing}")
else:
print("⚠️ Lint report not found.")
return missing

def scan\_and\_report():
print("Scanning for missing resources...")
refs = collect\_code\_references()
defs = collect\_defined\_resources()
missing = {t: refs\[t] - defs.get(t, set()) for t in refs if refs\[t] - defs.get(t, set())}
print(f"Missing by scan: {missing}")
return missing

def list\_resources():
print("Defined resources:")
defs = collect\_defined\_resources()
for typ, names in defs.items():
print(f"- {typ} ({len(names)})")
for n in sorted(names):
print(f"    • {n}")

def print\_references():
print("Code references:")
refs = collect\_code\_references()
for typ, names in refs.items():
print(f"- {typ} ({len(names)})")
for n in sorted(names):
print(f"    • {n}")

def generate\_stubs(missing):
print("Generating stubs...")
for d in \['drawable','dimen','id']:
for name in missing.get(d, \[]):
if d == 'drawable':
xml = (
'\<shape xmlns\:android="[http://schemas.android.com/apk/res/android](http://schemas.android.com/apk/res/android)" '
'android\:shape="rectangle"><size android:width="24dp" android:height="24dp"/>'
'<solid android:color="@android:color/transparent"/></shape>'
)
path = res\_dir / 'drawable' / f'{name}.xml'
path.write\_text(xml, encoding='utf-8')
print(f"Stub: drawable/{name}.xml")
elif d == 'dimen':
f = res\_dir / 'values' / 'dimens.xml'
text = f.read\_text()
entry = f'    <dimen name="{name}">16dp</dimen>\n'
if entry not in text:
f.write\_text(text.replace('</resources>', entry + '</resources>'))
print(f"Stub: dimen {name}")
elif d == 'id':
f = res\_dir / 'values' / 'ids.xml'
if not f.exists():
f.write\_text('<resources>\n</resources>', encoding='utf-8')
txt = f.read\_text()
entry = f'    <item name="{name}" type="id"/>\n'
if entry not in txt:
f.write\_text(txt.replace('</resources>', entry + '</resources>'))
print(f"Stub: id {name}")

def check\_syntax():
print("Checking Kotlin syntax...")
errors = collect\_syntax\_errors()
if errors:
for f, ln, msg in errors:
print(f"{f}:{ln} → {msg}")
else:
print("No syntax errors.")

def assemble():
\# Clean old Chaquopy pip cache
pip\_dir = project\_dir / 'app' / 'build' / 'python' / 'pip' / 'debug'
if pip\_dir.exists():
print("Cleaning old Chaquopy pip cache…")
shutil.rmtree(pip\_dir)
print("Building APK...")
proc = subprocess.run(\[GRADLEW, 'assembleDebug'], cwd=project\_dir)
if proc.returncode == 0:
apk = app\_dir / 'build' / 'outputs' / 'apk' / 'debug' / 'app-debug.apk'
print(f"Built: {apk}")
else:
print("Build failed.")

def html\_report(missing, errors, out):
print(f"Writing HTML report to {out}")
html = \['<html><body><h1>Android Health Report</h1>']
\# Missing Resources
if missing:
html.append('<h2>Missing Resources</h2>')
for t, names in missing.items():
html.append(f'<h3>{t}</h3><ul>')
for n in sorted(names):
html.append(f'<li>{n}</li>')
html.append('</ul>')
else:
html.append('<p>No missing resources.</p>')
\# Kotlin Syntax Errors
if errors:
html.append('<h2>Kotlin Syntax Errors</h2><ul>')
for f, ln, msg in errors:
html.append(f'<li>{f} (line {ln}): {msg}</li>')
html.append('</ul>')
else:
html.append('<p>No syntax errors.</p>')
\# XML Errors
xml\_errors = collect\_xml\_errors()
if xml\_errors:
html.append('<h2>XML Syntax Errors</h2><ul>')
for path, msg in xml\_errors:
html.append(f'<li>{path}: {msg}</li>')
html.append('</ul>')
else:
html.append('<p>No XML errors.</p>')
html.append('</body></html>')
Path(out).write\_text(''.join(html), encoding='utf-8')

def html\_resources(out):
print(f"Writing resources HTML to {out}")
defs = collect\_defined\_resources()
html = \['<html><body><h1>Defined Android Resources</h1>']
for typ, names in defs.items():
html.append(f'<h2>{typ} ({len(names)})</h2><ul>')
for n in sorted(names):
html.append(f'<li>{n}</li>')
html.append('</ul>')
html.append('</body></html>')
Path(out).write\_text(''.join(html), encoding='utf-8')

if **name** == '**main**':
parser = argparse.ArgumentParser()
parser.add\_argument('--lint', action='store\_true')
parser.add\_argument('--scan', action='store\_true')
parser.add\_argument('--stubs', action='store\_true')
parser.add\_argument('--html-report')
parser.add\_argument('--list-resources', action='store\_true')
parser.add\_argument('--print-refs', action='store\_true')
parser.add\_argument('--html-resources')
parser.add\_argument('--check-syntax', action='store\_true')
parser.add\_argument('--assemble', action='store\_true')
args = parser.parse\_args()

```
missing = {}
errors = []
if args.lint:
    missing = run_gradle_lint()
if args.scan:
    missing = scan_and_report()
if args.list_resources:
    list_resources()
if args.print_refs:
    print_references()
if args.html_resources:
    html_resources(args.html_resources)
if args.stubs and missing:
    generate_stubs(missing)
if args.check_syntax:
    check_syntax()
if args.assemble:
    assemble()
if args.html_report:
    errors = collect_syntax_errors()
    xml_errs = collect_xml_errors()
    html_report(missing, errors + xml_errs, args.html_report)

# Exit non-zero if any issues found
if missing or errors or xml_errs:
    sys.exit(1)
sys.exit(0)
```
