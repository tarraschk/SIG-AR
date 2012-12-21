for(var i = 0; i < 18; i++) { var scriptId = 'u' + i; window[scriptId] = document.getElementById(scriptId); }

$axure.eventManager.pageLoad(
function (e) {

});
gv_vAlignTable['u3'] = 'center';gv_vAlignTable['u16'] = 'top';u15.tabIndex = 0;

u15.style.cursor = 'pointer';
$axure.eventManager.click('u15', function(e) {

if (true) {

	SetPanelVisibility('u12','hidden','none',500);

}
});
gv_vAlignTable['u15'] = 'top';
u10.style.cursor = 'pointer';
$axure.eventManager.click('u10', function(e) {

if (true) {

	self.location.href=$axure.globalVariableProvider.getLinkUrl('Source_settings.html');

}
});

u17.style.cursor = 'pointer';
$axure.eventManager.click('u17', function(e) {

if (true) {

	self.location.href=$axure.globalVariableProvider.getLinkUrl('Main_Page.html');

}
});
gv_vAlignTable['u1'] = 'center';gv_vAlignTable['u9'] = 'center';gv_vAlignTable['u14'] = 'center';gv_vAlignTable['u6'] = 'center';
u11.style.cursor = 'pointer';
$axure.eventManager.click('u11', function(e) {

if (true) {

	SetPanelVisibility('u12','','none',500);

}
});
gv_vAlignTable['u7'] = 'top';