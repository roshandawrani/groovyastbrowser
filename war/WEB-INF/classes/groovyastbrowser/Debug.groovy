package groovyastbrowser

class Debug {
    static boolean DEBUG = false 
    static printNode(node, level = 0) {
        def skipRetTuple = Utils.skipNode(node, true) 
        if(skipRetTuple[0]) return
        print "  " * level
        println node
        node.children.each {
            printNode(it, level + 1)
        }
    }
    
    static tryAstBrowserWeb() {
        def script = """
            def x = 1
        """
        def astBrowser = new AstBrowserWeb(script, 5)
        astBrowser.showScriptClass = true
        astBrowser.showScriptFreeForm = true
        def astRoot = astBrowser.createAST()
        printNode(astRoot)
    }
    
    static log(message) {
       if(DEBUG) println message
    }
}