verifyReadableDirectory() {
   local path="${1}"

   [ -d "${path}" ] || semanticError "not a directory: ${path}"
   [ -x "${path}" ] || semanticError "directory not searchable: ${path}"
   [ -r "${path}" ] || semanticError "directory not readable: ${path}"
} && readonly -f verifyReadableDirectory

verifyWritableDirectory() {
   local path="${1}"

   verifyReadableDirectory "${path}"
   [ -w "${path}" ] || semanticError "directory not writable: ${path}"
} && readonly -f verifyWritableDirectory

handleOptions() {
   local options="hqv${1}"
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
} && readonly -f handleOptions

handleOption_h() {
   usageSummaryRequested=true
} && readonly -f handleOption_h

handleOption_q() {
   let currentLogLevel+=1
} && readonly -f handleOption_q

handleOption_v() {
   let currentLogLevel-=1
} && readonly -f handleOption_v

showUsageSummary() {
   semanticError "usage summary not available"
}

handlePositionalArguments() {
   [ "${#}" -eq 0 ] || syntaxError "too many positional arguments"
}

