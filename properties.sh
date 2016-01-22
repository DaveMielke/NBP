tagPrefix="B2G_UI"

revisionIdentifier="$(git describe --long --tags --abbrev=16 --match "${tagPrefix}-*")"
set -- ${revisionIdentifier//-/ }
revisionVersion="${2}"
revisionIncrement="${3}"
revisionCommit="${4#g}"

set -- ${revisionVersion//./ }
versionMajor="${1}"
versionMinor="${2}"

set -- $(date --utc +'%Y-%m-%d %H:%M %Z')
buildDate="${1}"
buildTime="${2}"
buildZone="${3}"

packageVersion="${versionMajor}.${versionMinor}.${revisionIncrement}"
sourceRevision="git:${revisionCommit}"
