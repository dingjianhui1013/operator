//对话框 dialog
function openDialog(elementId) {
    var z_index = 101;
    $(".ui-widget-overlay").each(function(){
        var cur_z_index = parseInt($(this).css("z-index"));
        if( cur_z_index >= z_index){
            z_index = cur_z_index + 1;
        }
    });
    $("<div class='ui-widget-overlay' id='" + elementId + "_overlay'></div>")
        .appendTo($("body")).css("z-index",z_index).css("height",$("#box").height());
    $("#" + elementId).css("z-index",z_index +1).show();
}
function closeDialog(elementId) {
    $("#" + elementId).css("z-index", 0).hide();
    $("#" + elementId + "_overlay").remove();
}

function showMessage(message) {
    $("#message_dialog_content").text(message);
    var left = ($("body").width() - $("#top_message_dialog").width()) / 2;
    var top = ($("body").height() - $("#top_message_dialog").height()) / 3;
    $("#top_message_dialog").css("left", left);
    $("#top_message_dialog").css("top", top);
    openDialog("top_message_dialog")
}

function showSpecDiv(divId, className) {
    $("." + className).hide();
    $("#" + divId).show();
}

function hideDiv(className) {
    $("." + className).hide();
}
