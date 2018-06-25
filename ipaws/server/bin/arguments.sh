verifyIntegerValue() {
   local value="${1}"
   local description="${2}"
   local minimum="${3}"
   local maximum="${4}"

   description+=" value"
   [[ "${value}" =~ ^(0|-?[1-9][0-9]*)$ ]] || syntaxError "invalid ${description}: ${value}"
   [ -n "${minimum}" ] && [ "${value}" -lt "${minimum}" ] && semanticError "${description} less than ${minimum}: ${value}"
   [ -n "${maximum}" ] && [ "${value}" -gt "${maximum}" ] && semanticError "${description} greater than ${maximum}: ${value}"
   return 0
} && readonly -f verifyIntegerValue

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

handleCommandArguments() {
   local options="${1}"
   local positional="${2}"
   shift 2

   local usageSummaryRequested=false
   local logLevelAdjustment=0
   local option

   while getopts ":hqv${options}" option
   do
      case "${option}"
      in
         :) syntaxError "missing operand: -${OPTARG}";;
        \?) syntaxError "unrecognized option: -${OPTARG}";;
         *) "handleCommandOption_${option}";;
      esac
   done

   "${usageSummaryRequested}" && {
      [ -z "${positional}" ] || positional=" ${positional}"
      echo "Usage: ${programName} [-option ...]${positional}"
      showCommonCommandOptionsUsageSummary

      [ -n "${options}" ] && {
         echo -e "\nThese options are specific to this command:"
         showCommandSpecificOptionsUsageSummary
      }

      exit 0
   }

   shift $((OPTIND - 1))
   handlePositionalCommandArguments "${@}"
   let IPAWS_LOG_LEVEL+=logLevelAdjustment || :
} && readonly -f handleCommandArguments

showCommonCommandOptionsUsageSummary() {
cat <<END-OF-COMMON-OPTIONS-USAGE-SUMMARY

These options are common to all commands:
-h  (help) show this command usage summary on standard output and then exit
-q  (quiet) decrease logging verbosity
-v  (verbose) increase logging verbosity
END-OF-COMMON-OPTIONS-USAGE-SUMMARY
} && readonly -f showCommonCommandOptionsUsageSummary

handleCommandOption_h() {
   usageSummaryRequested=true
} && readonly -f handleCommandOption_h

handleCommandOption_q() {
   let logLevelAdjustment+=1 || :
} && readonly -f handleCommandOption_q

handleCommandOption_v() {
   let logLevelAdjustment-=1 || :
} && readonly -f handleCommandOption_v

handlePositionalCommandArguments() {
   [ "${#}" -eq 0 ] || syntaxError "too many positional arguments"
}

