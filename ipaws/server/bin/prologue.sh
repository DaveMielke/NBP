set -e
shopt -s nullglob

readonly programName="${0##*/}"
programDirectory="${0%/*}"
[ "${programDirectory}" = "${programDirectory}" ] && programDirectory="."
readonly programDirectory="$(realpath "${programDirectory}")"

. "${programDirectory}/scripts.sh"
. "${programDirectory}/arguments.sh"
. "${programDirectory}/strings.sh"
. "${programDirectory}/xml.sh"

