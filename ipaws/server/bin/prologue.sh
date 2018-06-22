set -e
shopt -s nullglob

readonly programName="${0##*/}"
programDirectory="${0%/*}"
[ "${programDirectory}" = "${programDirectory}" ] && programDirectory="."
readonly programDirectory="$(realpath "${programDirectory}")"

readonly serverDirectory="${programDirectory%/*}"
readonly defaultConfigurationDirectory="${serverDirectory}/etc"
readonly defaultDataDirectory="${serverDirectory}/var"

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

verifyReadableDirectory() {
   local path="${1}"

   [ -d "${path}" ] || semanticError "not a directory: ${path}"
   [ -x "${path}" ] || semanticError "directory not searchable: ${path}"
   [ -r "${path}" ] || semanticError "directory not readable: ${path}"
}

verifyWritableDirectory() {
   local path="${1}"

   verifyReadableDirectory "${path}"
   [ -w "${path}" ] || semanticError "directory not writable: ${path}"
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

xpathGet() {
   local file="${1}"
   local xpath="${2}"

   xpath -q -e "${xpath}" "${file}"
}

readonly xpathAlert="/ns1:alerts/alert"

ipawsGetAlertCount() {
   local file="${1}"

   xpathGet "${file}" "count(${xpathAlert})"
}

ipawsGetAlertElement() {
   local file="${1}"
   local index="${2}"

   xpathGet "${file}" "${xpathAlert}[${index}]"
}

