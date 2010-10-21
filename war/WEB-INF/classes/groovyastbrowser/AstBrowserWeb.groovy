package groovyastbrowser

import groovy.inspect.TextTreeNodeMaker
import groovy.inspect.swingui.ScriptToTreeNodeAdapter

class AstBrowserWeb {
    def script
    def compilePhase
    boolean showScriptClass
    boolean showScriptFreeForm
    
    static {
        def renderProps = ScriptToTreeNodeAdapter.classNameToStringForm
        if(!renderProps) {
            renderProps.putAll(Utils.getClassNameToStringFormProperties())
        }
    }
    
    AstBrowserWeb(String script, compilePhase) {
        this.script = script
        this.compilePhase = compilePhase
    }
    
    def createAST() {
        if(!script) {
            return null
        }
        
        Debug.log "script = $script, compilePhase = $compilePhase, showScriptClass = $showScriptClass, showScriptFreeForm = $showScriptFreeForm"

        def nodeMaker = new TextTreeNodeMaker() 

        def adapter = new ScriptToTreeNodeAdapter(null, showScriptFreeForm, showScriptClass, nodeMaker)
        
        adapter.compile(script, compilePhase)
    }
    
    static main(args) {
        Debug.tryAstBrowserWeb()
    }
}
