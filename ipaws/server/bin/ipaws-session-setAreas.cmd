includeScriptLibraries sql

ipawsSession_setAreas() {
   local -A areas
   areas["000000"]=1
   local area

   for area
   do
      [[ "${area}" =~ ^[0-9]{6}$ ]] || continue

      if [[ "${area}" =~ ^0..0{3}$ ]]
      then
         areas["${area:1:2}"]=1
      else
         areas["${area}"]=1

         area="0${area#?}"
         areas["${area}"]=1

         area="${area%???}000"
         areas["${area}"]=1
      fi
   done

   local command="begin transaction;"
   command+=" delete from requested_areas where client='${clientReference}';"

   for area in "${!areas[@]}"
   do
      command+=" insert into requested_areas (client, SAME) values ('${clientReference}', '${area}');"
   done

   command+=" commit;"
   sqlExecute "${command}"
}

