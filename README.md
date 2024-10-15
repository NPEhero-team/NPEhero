# NPE Hero v2.0
One-year anniversary update!

Goals:
- [x] New integrated level editor
- [X] Custom keybindings
- [X] Accept mp3 song files
- [ ] UI improvements (- 2)
- [ ] Code cleanup (- 3)
- [X] Installer / Linux package

Todo - needs design:
- Improve errorList and error handling
- Improve aesthetic of songplayer
- overhaul noteseditor2
- improve resizing (songplayer and noteseditor2)

Todo - bugs:
- fix reliance on local font
- Properly center background image
- fix or remove noteseditor1

# Building
### Run the app
Run the Driver class from your IDE (known to work with Intellij)

OR

Execute the maven goal javafx:run
- windows: `mvnw.cmd javafx:run`
- linux: `./mvnw javafx:run`

### Create an installer for your OS
Prerequisites:

Ensure that all the following packages are installed and the correct version

- java >= 22
- jmod >= 22
- jlink >= 22
- jpackage >= 22

note: check the version of these with `<package> --version`

You will also need OS specific jpackage prereqs to create the executable

- RPM, DEB on Linux: On Red Hat Linux, we need the rpm-build package; on Ubuntu Linux, we need the fakeroot package
- PKG, DMG on macOS: Xcode command line tools are required when the –mac-sign option is used to request that the package be signed, and when the –icon option is used to customize the DMG image
- EXE, MSI on Windows: On Windows, we need the third party tool WiX 3.0 or later

[source](https://www.baeldung.com/java14-jpackage#packaging-prerequisite)


Execute the maven package phase
- windows: `mvnw.cmd package`
- linux: `./mvnw package`

The installer executables should output to `target/dist`

# Development Links

[Figma link](https://www.figma.com/file/dpeMlWStSWrVHfLd0Uohws/Untitled?node-id=0%3A1&t=PVQi61Ig3AWtWNMm-1)

[JavaFX docs (with search)](https://openjfx.io/javadoc/15)

javafx guides (really helpful website):
[edencoding.com](https://edencoding.com/javafx-layouts/)
