tagPrefix="B2G_UI"

revisionIdentifier="$(git describe --long --tags --abbrev=16 --match "${tagPrefix}-*")"
set -- ${revisionIdentifier//-/ }
revisionVersion="${2}"
revisionIncrement="${3}"
revisionCommit="${4#g}"

set -- ${revisionVersion//./ }
versionMajor="${1}"
versionMinor="${2}"
