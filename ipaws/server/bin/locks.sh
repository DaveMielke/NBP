verifyCommandAvailability flock

declare -A lockNames=()

openLockFile() {
   local olfLock="${1}"
   local olfName="${2}"
   local olfDescriptor=10

   while :
   do
      [ -e "/dev/fd/${olfDescriptor}" ] || break
      let olfDescriptor+=1
   done

   eval exec "${olfDescriptor}>>" '"${dataDirectory}/${olfName}.lock"'
   setVariable "${olfLock}" "${olfDescriptor}"
} && readonly -f openLockFile

closeLockFile() {
   local lock="${1}"

   eval exec "${lock}>&-"
} && readonly -f closeLockFile

acquireLock() {
   local aqlVariable="${1}"
   local aqlName="${2}"
   local aqlExclusive="${3}"
   local aqlWait="${4}"

   local aqlTypeWord aqlTypeOption
   if "${aqlExclusive}"
   then
      aqlTypeWord="exclusive"
      aqlTypeOption="-e"
   else
      aqlTypeWord="shared"
      aqlTypeOption="-s"
   fi

   local aqlActionWord aqlActionOption
   if "${aqlWait}"
   then
      aqlActionWord="acquiring"
      aqlActionOption=""
   else
      aqlActionWord="attempting"
      aqlActionOption="-n"
   fi

   local aqlLock
   openLockFile aqlLock "${aqlName}"
   logDebug "${aqlActionWord} ${aqlTypeWord} lock: ${aqlName}"

   flock ${aqlTypeOption} ${aqlActionOption} "${aqlLock}" || {
      logDebug "lock failed: ${aqlName}"
      closeLockFile "${aqlLock}"
      return 1
   }

   lockNames[${aqlLock}]="${aqlName}"
   logDebug "lock acquired: ${aqlName}"
   setVariable "${aqlVariable}" "${aqlLock}"
} && readonly -f acquireLock

acquireExclusiveLock() {
   local axlVariable="${1}"
   local axlName="${2}"

   acquireLock "${axlVariable}" "${axlName}" true true
} && readonly -f acquireExclusiveLock

acquireSharedLock() {
   local aslVariable="${1}"
   local aslName="${2}"

   acquireLock "${aslVariable}" "${aslName}" false true
} && readonly -f acquireSharedLock

attemptExclusiveLock() {
   local axlVariable="${1}"
   local axlName="${2}"

   acquireLock "${axlVariable}" "${axlName}" true false
} && readonly -f attemptExclusiveLock

attemptSharedLock() {
   local aslVariable="${1}"
   local aslName="${2}"

   acquireLock "${aslVariable}" "${aslName}" false false
} && readonly -f attemptSharedLock

releaseLock() {
   local lock="${1}"
   local -n name="lockNames[${lock}]"

   if [ -n "${name}" ]
   then
      flock -u "${lock}"
      closeLockFile "${lock}"
      logDebug "lock released: ${name}"
      unset name
   else
      logWarning "lock not held: fd=${lock}"
   fi
} && readonly -f releaseLock

