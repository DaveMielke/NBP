set -e
shopt -s nullglob

readonly programName="${0##*/}"
programDirectory="${0%/*}"
[ "${programDirectory}" = "${programDirectory}" ] && programDirectory="."
readonly programDirectory="$(realpath "${programDirectory}")"

programMessage() {
   local message="${1}"

   [ -z "${message}" ] || echo "${programName}: ${message}"
}

syntaxError() {
   local message="${1}"

   programMessage "${message}"
   exit 2
}

semanticError() {
   local message="${1}"

   programMessage "${message}"
   exit 3
}

responseError() {
   local message="${1}"

   programMessage "${message}"
   exit 4
}

handleOptions() {
   local options="h${1}"
   shift 1

   local usageSummaryRequested=false
   local option

   while getopts ":${options}" option
   do
      case "${option}"
      in
         :) syntaxError "missing operand: -${OPTARG}";;
        \?) syntaxError "unrecognized option: -${OPTARG}";;
         *) "handleOption_${option}";;
      esac
   done

   "${usageSummaryRequested}" && {
      showUsageSummary
      exit 0
   }

   shift $((OPTIND - 1))
   handlePositionalArguments "${@}"
}

handleOption_h() {
   usageSummaryRequested=true
}

showUsageSummary() {
   semanticError "usage summary not available"
}

handlePositionalArguments() {
   [ "${#}" -eq 0 ] || syntaxError "too many positional arguments"
}

