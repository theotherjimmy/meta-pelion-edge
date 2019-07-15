#!/bin/sh
/wigwag/wwrelay-utils/debug_scripts/create-new-eeprom-with-self-signed-certs.sh
for tmpl in /wigwag/etc/node-composite/*.tmpl ; do
  mustache /userdata/edge_gw_config/identity.json $tmpl > ${tmpl/%.tmpl/.json}
done
