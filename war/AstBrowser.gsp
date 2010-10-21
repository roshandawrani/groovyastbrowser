<%
    import groovyastbrowser.*
%>
<html>
    <head>
        <title>Groovy AST Browser</title>

        <script src="/js/jquery-1.3.2.min.js" type="text/javascript"></script>
        <script src="/js/jquery-ui-1.7.2.custom.min.js" type="text/javascript"></script>
        
        <script src="js/mktree.js" type="text/javascript"></script>
        <script src="/js/codemirror.js" type="text/javascript"></script>
        <script src="/js/mirrorframe.js" type="text/javascript"></script>
        <script src="js/astbrowser.js" type="text/javascript"></script>

        <link rel="stylesheet" type="text/css" href="/css/redmond/jquery-ui-1.7.1.custom.css"/>        
        <link href="css/astbrowser.css" type="text/css" rel="stylesheet">
        <link href="css/mktree.css" type="text/css" rel="stylesheet">
    </head>

    <body>
        <form id="scriptForm" action="GetAST.gsp">
            <table width=100% border=1>
                <tr>
                    <td class="tdpadded" width=50%>
                        At end of Phase:
                        <select id="compilationPhase" title="After selecting a phase, click 'Show AST' to see AST after that phase">
                          <%= Utils.getCompilationPhasesOptionsHTML() %>
                        </select>
                    </td>
                    <td>
                        <span title="Do you want to see class members internally added by groovy(synthetic)?">
                            <input type="checkbox" id="includeSynthetic" value="true" checked>Include Synthetic Members<br>
                        </span>
                    </td>
                    <td align="center">
                        <div id="firefoxRecommend">Warning: Firefox browser recommended</div>
                        <div id="loadingDiv">
                            <img src="images/rotating_arrow.gif">
                        </div>        
                    </td>
                </tr>
            </table>
            <div class="leftdiv">
                <div id="scriptArea" class="border">
                    <textarea rows="28" height= "460px" id="scriptText" value="type code here" wrap='off'></textarea>
                </div>
            </div>

            <div class="rightdiv" class="border">
                <div id="astTreeDiv">
                    <ul id="astTreeUL" class='mktree'>
                        <li class="liBullet">empty</li>
                    </ul>
                </div>
            </div>

            <table width=100% border=1>
                <tr>
                    <td align="center" width=50%>
                        <table width=50%>
                            <tr>
                                <td><button type="button" id="executeButton" onclick="javascript:void(0)">Show AST</button></td>
                                <td><button type="button" id="clearButton" onclick="javascript:void(0)">Clear</button></td>
                                <td><button type="button" id="backButton" onclick="javascript:void(0)">Back</button></td>
                            </tr>
                        </table>
                    </td>
                    <td align="right" class="tdpadded">
                        <%= Utils.GROOVY_VERSION %>&nbsp;&nbsp;
                        <a href="mailto:roshandawrani@codehaus.org" 
                        title="Send your feedback at roshandawrani@codehaus.org">Feedback</a>
                    </td>
                </tr>
            </table>
        </form>

        <script language="javascript">
            var editor = CodeMirror.fromTextArea('scriptText', {
                parserfile: ["tokenizejavascript.js", "parsejavascript.js"],
                stylesheet: "/css/jscolors.css",
                path: "/js/",
                continuousScanning: 500,
                textWrapping: false,
                height: "455px",
                tabMode: "spaces",
                lineNumbers: true,
                submitFunction: function() {
                    \$("#executeButton").click();
                }
            });
        </script>
    </body> 
</html>