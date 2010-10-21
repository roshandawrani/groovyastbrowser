var isFireFox = isBrowserFireFox(); 
$(document).ready(function() {
    if(isFireFox)
        $('#firefoxRecommend').hide();
    else
        $('#firefoxRecommend').show();
        
    $("#clearButton").click(function(event) {
        editor.setCode("");
        $("#astTreeUL").html("<li>empty<ul></ul></li>");
        convertTrees();
        return false;
    });

    $("#backButton").click(function() {
        history.back();
    });
    
    $("#executeButton1").click(function(event) {
        var code = editor.getCode();
    
        if(code.replace(/^\s+|\s+$/g, '').length > 0) {
            $("#scriptForm").submit();
        } else {
            alert("Please enter a script first.")
            return;
        }
    });

    $("#executeButton").click(function(event) {
        var code = editor.getCode();
    
        if(!code.replace(/^\s+|\s+$/g, '').length > 0) {
            alert("Please enter a script first.")
            return;
        }
        $.ajax({
            type: "POST",
            url: "/GetAST.gsp",
            data: { script: editor.getCode(), phase: $("#compilationPhase").val(), 
                synthetic: $("#includeSynthetic").attr('checked') },
            dataType: "html",
            
            success: function(data) {
                $("#astTreeUL").html(data);
                convertTrees();
                listItemsColorChange();
            },

            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert("Error occured: " + errorThrown);
            }

        });
    });

    $('#loadingDiv')
        .hide()
        .ajaxStart(function() {
            $("#astTreeUL").html("<li class='liBullet'>loading...<ul></ul></li>");
            convertTrees();
            $('#firefoxRecommend').hide();
            $(this).show();
        })
        .ajaxStop(function() {
            $(this).hide();
            if(!isFireFox) $('#firefoxRecommend').show();
        });

        listItemsColorChange();
});

function listItemsColorChange() {
    var MOUSE_ENTER = {'color': '#BA0000'};
    var MOUSE_LEAVE = {'color': 'black'};
    $('span.bullet')
        .mouseenter(function() {
            $(this).css(MOUSE_ENTER);
        })
        .mouseleave(function() {
            $(this).css(MOUSE_LEAVE);
        });

    $('li.liBullet')
        .mouseenter(function() {
            $(this).css(MOUSE_ENTER);
        })
        .mouseleave(function() {
            $(this).css(MOUSE_LEAVE);
        });
}

function isBrowserFireFox() {
    var ua = navigator.userAgent;
    return ua.indexOf('Firefox') != -1 && ua.indexOf('Navigator') == -1;
}