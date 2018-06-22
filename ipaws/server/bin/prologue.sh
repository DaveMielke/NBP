set -e
shopt -s nullglob

readonly programName="${0##*/}"
programDirectory="${0%/*}"
[ "${programDirectory}" = "${programDirectory}" ] && programDirectory="."
readonly programDirectory="$(realpath "${programDirectory}")"

declare -A includedScriptLibraries=()
includeScriptLibraries() {
   local name

   for name
   do
      local -n included="includedScriptLibraries["${name}"]"

      [ -z "${included}" ] && {
         included=1
         . "${programDirectory}/${name}.sh"
      }

      unset -n included
   done
}

