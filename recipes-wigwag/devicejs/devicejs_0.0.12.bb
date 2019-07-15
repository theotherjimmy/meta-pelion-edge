DESCRIPTION = "devicejs message bus"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

inherit pkgconfig gitpkgv npm-base npm-install systemd

PR = "r4"
SRC_URI = "git://git@github.com/armPelionEdge/devicejs-ng.git;protocol=ssh;branch=master\
           file://devicejs.service\
"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICES_${PN} = "devicejs.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

DEPENDS = "nodejs node-native udev avahi"
RDEPENDS_${PN} += " nodejs"

BBCLASSEXTEND = "native"

INHIBIT_PACKAGE_STRIP = "1"  

FILES_${PN} = "/wigwag/* ${systemd_system_unitdir}/devicejs.service"

do_configure(){
	oe_runnpm_native install -g node-gyp
}

do_compile() {
  cp ${STAGING_INCDIR}/avahi-compat-libdns_sd/dns_sd.h ${STAGING_INCDIR}/
  ARCH=`echo $AR | awk -F '-' '{print $1}'`
  PLATFORM=`echo $AR | awk -F '-' '{print $3}'`
  export npm_config_arch=$ARCH
  cd ${S}
  oe_runnpm install --production
}

do_install() {
  cd ${S}
  install -d ${D}/wigwag
  install -d ${D}/wigwag/etc
  install -d ${D}/wigwag/devicejs-ng
  cp -r * ${D}/wigwag/devicejs-ng
  install -d ${D}${systemd_system_unitdir}
  install -m 644 ${WORKDIR}/devicejs.service ${D}${systemd_system_unitdir}
}
