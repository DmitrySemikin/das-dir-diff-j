This dir contains files, which can be used as templates for argument list files
to start the application with gradle like this:
run --args="@../00_run-args/run_help.args"

Corresponding configurations are also stored in git and should be available.

To have actual argument files copy "*.template" file without ".template"
extension and adjust the argument values as needed (on a first place - directory and file paths).
The non-template files are ignored by Git.
