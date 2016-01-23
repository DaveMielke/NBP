tagPrefix="B2G_UI"

revisionIdentifier="$(git describe --long --tags --abbrev=16 --match "${tagPrefix}-*")"
set -- $(echo "${revisionIdentifier}" | sed 's/-/ /g')
revisionVersion="${2}"
revisionIncrement="${3}"
revisionCommit="${4#g}"

set -- $(echo "${revisionVersion}" | sed 's/\./ /g')
versionMajor="${1}"
versionMinor="${2}"

set -- $(date --utc +'%Y-%m-%d %H:%M %Z')
buildDate="${1}"
buildTime="${2}"
buildZone="${3}"

versionCode=$(((versionMajor << 24) | (versionMinor << 16) | revisionIncrement))
versionNumber="${versionMajor}.${versionMinor}.${revisionIncrement}"
sourceRevision="git:${revisionCommit}"
