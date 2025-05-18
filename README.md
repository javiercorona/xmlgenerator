# android-health-cli

**Android Health Checker CLI**

A command-line tool for inspecting the health of your Android projects. It helps you:

* 🔍 **Scan** source and resource folders for missing drawables, layouts, strings, colors, IDs, and dimensions.
* 🛠️ **Generate** stub resource files automatically to satisfy missing references.
* 📝 **Run** Gradle lint to catch missing-resource issues.
* 🐞 **Check** Kotlin syntax errors by invoking the compile task.
* 📄 **Validate** XML resource files for syntax errors.
* 📦 **Assemble** your APK with a single command.
* 📊 **Report** all findings in a rich HTML summary.

---

## 🚀 Installation

```bash
# Clone the repo and install locally
git clone https://github.com/javiercorona/xmlgenerator.git
cd xmlgenerator
pip install .

# (Optional) Install the released package
pip install android-health-cli
```

## 📖 Usage

Once installed, you can run the `android-health` command with the following subcommands:

```bash
# See global help and available commands
android-health --help
```

### Subcommands

| Command                | Description                                                 |
| ---------------------- | ----------------------------------------------------------- |
| `scan`                 | Scan code & res directories for missing resources.          |
| `lint`                 | Run `./gradlew lint` and parse out missing-resource issues. |
| `stubs`                | Generate stub XML or values entries for missing resources.  |
| `check-syntax`         | Compile Kotlin sources to report syntax errors.             |
| `assemble`             | Run `./gradlew assembleDebug` to build the debug APK.       |
| `report --html-report` | Produce a comprehensive HTML health report.                 |

```bash
# Example: Scan and stub missing resources
a·ndroid-health scan --project-dir ./MyApp \
              && android-health stubs --project-dir ./MyApp

# Example: Full health report
android-health report --html-report myapp_health.html --project-dir ./MyApp
```

## ⚙️ Configuration

By default, the tool looks for:

* A `settings.gradle` or `build.gradle` to locate your project root.
* An `app/src/main/res` folder for resources.
* The Gradle wrapper (`gradlew` / `gradlew.bat`) to run lint and assemble.

You can override paths with:

```bash
android-health scan --project-dir /path/to/root
```

## 🧪 Testing & Development

1. Clone and install in editable mode:

   ```bash
   git clone https://github.com/javiercorona/xmlgenerator.git
   cd xmlgenerator
   pip install -e .
   ```
2. Run unit tests:

   ```bash
   pytest
   ```
3. Lint and type-check:

   ```bash
   flake8 .
   mypy .
   ```

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repo.
2. Create a feature branch (`git checkout -b feature/...`).
3. Commit your changes (`git commit -m "Add ..."`).
4. Push to your branch (`git push`).
5. Open a Pull Request.

Read [CONTRIBUTING.md](CONTRIBUTING.md) for more details.

## 📄 License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.
