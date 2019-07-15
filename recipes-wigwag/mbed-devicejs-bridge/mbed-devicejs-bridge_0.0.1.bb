DESCRIPTION = "mbed bridge"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://bridge/LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"
inherit autotools pkgconfig gitpkgv npm-base npm-install systemd

PV = "1.0+git${SRCPV}"
PKGV = "1.0+git${GITPKGV}"
SRCREV = "${AUTOREV}"
SRC_URI = "git://git@github.com/armPelionEdge/mbed-devicejs-bridge;protocol=ssh;branch=master"
PR = "r2"


SRC_URI="git://git@github.com/armPelionEdge/mbed-devicejs-bridge;protocol=ssh;branch=master;name=bridge;destsuffix=git/bridge \
  git://git@github.com/armPelionEdge/mbed-edge-websocket.git;protocol=ssh;branch=master;name=edgejs;destsuffix=git/edgejs \
  file://config-dev.json\
  file://mbed-devicejs-bridge.service\
  file://node-mbed-composite.json\
"
SRCREV_FORMAT = "bridge-edgejs"
SRCREV_bridge = "${AUTOREV}"
SRCREV_edgejs = "${AUTOREV}"


DEPENDS = "nodejs node-native"
RDEPENDS_${PN} += " nodejs bash"

S = "${WORKDIR}/git"

BBCLASSEXTEND = "native"

INHIBIT_PACKAGE_STRIP = "1"  

INSANE_SKIP_${PN} += "arch"
FILES_${PN} += "/wigwag/*" 

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "mbed-devicejs-bridge.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_compile() {
    ARCH=`echo $AR | awk -F '-' '{print $1}'`
    PLATFORM=`echo $AR | awk -F '-' '{print $3}'`
    export npm_config_arch=$ARCH
    cd ${S}/bridge
    cp devicejs.json package.json
    oe_runnpm --arch=arm --target_arch=arm --target_platform=linux install 
    rm -rf node_modules/mbed-cloud-sdk/.venv
    cd ${S}/edgejs
    oe_runnpm --arch=arm --target_arch=arm --target_platform=linux install 
}

do_install() {
    cd ${S}/bridge
    install -d ${D}/wigwag
    install -d ${D}/wigwag/mbed
    install -d ${D}/wigwag/mbed/mbed-devicejs-bridge
    cp -r * ${D}/wigwag/mbed/mbed-devicejs-bridge
    cd ${S}/edgejs
    install -d ${D}/wigwag/mbed/mbed-edge-websocket
    cp -r * ${D}/wigwag/mbed/mbed-edge-websocket    
    cp ${S}/../config-dev.json ${D}/wigwag/mbed/mbed-devicejs-bridge/config.json
    install -d ${D}/wigwag/etc/node-composite/
    install -d ${D}${systemd_system_unitdir}
    install -m 644 ${WORKDIR}/mbed-devicejs-bridge.service ${D}${systemd_system_unitdir}
    install -m 644 ${WORKDIR}/node-mbed-composite.json ${D}/wigwag/etc/node-composite/mbed.tmpl
}
