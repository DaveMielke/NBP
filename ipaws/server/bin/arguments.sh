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

