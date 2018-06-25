includeScriptLibraries xml

defineEnumeration capUrgencyEnumeration Immediate Expected Future Past Unknown
defineEnumeration capSeverityEnumeration Extreme Severe Moderate Minor Unknown
defineEnumeration capCertaintyEnumeration Observed Likely Possible Unlikely Unknown

readonly capAlertElement="/ns1:alerts/alert"

capGetAlertCount() {
   local alerts="${1}"

   xmlEvaluate "${alerts}" "count(${capAlertElement})"
} && readonly -f capGetAlertCount

capGetAlertElement() {
   local alerts="${1}"
   local index="${2}"

   xmlEvaluate "${alerts}" "${capAlertElement}[${index}]"
} && readonly -f capGetAlertElement

