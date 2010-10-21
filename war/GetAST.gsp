<%
    import groovyastbrowser.*
    
    def astRoot = null
    def script = request.getParameter('script')
    def includeSynthetic = true
    
    if(script) {
        def compilationPhase = request.getParameter('phase')
        includeSynthetic = Boolean.parseBoolean(request.getParameter('synthetic'))
        
        def astBrowser = new AstBrowserWeb(script, Integer.parseInt(compilationPhase))
        astBrowser.showScriptClass = true
        astBrowser.showScriptFreeForm = true

        astRoot = astBrowser.createAST()
    }
%>
<%= Utils.convertToHTML(astRoot, includeSynthetic) %>
