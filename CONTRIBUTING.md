# Contributing to IPTV Master

First off, thank you for considering contributing to IPTV Master! It's people like you that make this project great.

## Code of Conduct

By participating in this project, you agree to abide by our [Code of Conduct](CODE_OF_CONDUCT.md). Please read it before contributing.

## How Can I Contribute?

### Reporting Bugs

Before creating a bug report, please check the existing issues to see if the problem has already been reported. When creating a bug report, include as many details as possible:

- **Clear and descriptive title**
- **Steps to reproduce** the issue
- **Expected behavior** vs **actual behavior**
- **Screenshots** or screen recordings if applicable
- **Device and Android version** you're using
- **App version** you're running

### Suggesting Features

Feature suggestions are welcome! When suggesting a feature:

- Explain **why** this feature would be useful
- Describe **how** it should work
- Include **mockups** or examples if applicable

### Pull Requests

1. **Fork** the repository
2. **Create a new branch** from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. **Make your changes**
4. **Test your changes** thoroughly
5. **Commit** with a clear and descriptive message:
   ```bash
   git commit -m "feat: add your feature description"
   ```
6. **Push** to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```
7. **Open a Pull Request** targeting the `main` branch

### Commit Message Convention

We follow [Conventional Commits](https://www.conventionalcommits.org/):

```
type(scope): description

- feat:     A new feature
- fix:      A bug fix
- docs:     Documentation changes
- style:    Code style changes (formatting, etc.)
- refactor: Code refactoring
- perf:     Performance improvements
- test:     Adding or fixing tests
- chore:    Build process or tool changes
```

Example: `feat(player): add gesture controls for brightness`

### Code Style

- **Kotlin**: Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- **Naming**: Use descriptive names for variables, functions, and classes
- **Formatting**: Use 4-space indentation
- **Comments**: Write clear comments for complex logic
- **Tests**: Write unit tests for new functionality

### Development Setup

1. Install **Android Studio Hedgehog** (2023.1.1) or later
2. Clone the repository:
   ```bash
   git clone https://github.com/iptvmaster/iptvmaster.git
   ```
3. Open the project in Android Studio
4. Let Gradle sync and download dependencies
5. Create a `local.properties` file pointing to your Android SDK:
   ```
   sdk.dir=/path/to/Android/Sdk
   ```

### Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/iptvmaster/
│   │   │   ├── data/          # Data layer (repositories, models)
│   │   │   ├── domain/        # Domain layer (use cases, entities)
│   │   │   ├── ui/            # UI layer (screens, viewmodels)
│   │   │   └── di/            # Dependency injection modules
│   │   └── res/               # Resources
│   └── test/                  # Unit tests
├── build.gradle.kts
└── ...
```

## Questions?

If you have any questions, feel free to open an issue or reach out to the maintainers.

Thank you for contributing! 🎉
