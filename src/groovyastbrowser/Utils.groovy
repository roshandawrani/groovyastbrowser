package groovyastbrowser

class Utils {
    static final COMPILATION_PHASES_OPTIONS
    static final GROOVY_VERSION = "Groovy: ${GroovySystem.version}"

    static {
        def buffer = new StringBuffer()
        
        buffer.append("""<option value="1" >Initialization</option>""")
        buffer.append("""<option value="2" >Parsing</option>""")
        buffer.append("""<option value="3" >Conversion</option>""")
        buffer.append("""<option SELECTED value="4" >Semantic Analysis</option>""")
        buffer.append("""<option value="5" >Canonicalization</option>""")
        buffer.append("""<option value="6" >Instruction Selection</option>""")
        buffer.append("""<option value="7" >Class Generation</option>""")
        
        COMPILATION_PHASES_OPTIONS = buffer.toString()
    }

    static getClassNameToStringFormProperties() {
        def customConfig = new ConfigSlurper().parse("""
            org {
                codehaus {
                    groovy {
                        ast {
                            ClassNode           = 'ClassNode - \$expression.name'
                            InnerClassNode      = 'InnerClassNode - \$expression.name'
                            ConstructorNode     = 'ConstructorNode - \$expression.name'
                            MethodNode          = 'MethodNode - \$expression.name'
                            FieldNode           = 'FieldNode - \$expression.name : \$expression.type'
                            PropertyNode        = 'PropertyNode - \${expression.field?.name} : \${expression.field?.type}'
                            AnnotationNode      = 'AnnotationNode - \${expression.classNode?.name}'
                            Parameter           = 'Parameter - \$expression.name'
                            DynamicVariable     = 'DynamicVariable - \$expression.name'
            
                            stmt {
                                BlockStatement      = 'BlockStatement - (\${expression.statements ? expression.statements.size() : 0})'
                                ExpressionStatement = 'ExpressionStatement - \${expression?.expression.getClass().simpleName}'
                                ReturnStatement     = 'ReturnStatement - \$expression.text'
                                TryCatchStatement   = 'TryCatchStatement - \${expression.catchStatements?.size ?: 0} catch, \${expression.finallyStatement ? 1 : 0} finally'
                                CatchStatement      = 'CatchStatement - \$expression.exceptionType]'
                            }
                            expr {
                                ConstructorCallExpression   = 'ConstructorCall - \$expression.text'
                                SpreadExpression        = 'Spread - \$expression.text'
                                ArgumentListExpression  = 'ArgumentList - \$expression.text'
                                MethodCallExpression    = 'MethodCall - \$expression.text'
                                GStringExpression       = 'GString - \$expression.text'
                                AttributeExpression     = 'Attribute - \$expression.text'
                                DeclarationExpression   = 'Declaration - \$expression.text'
                                VariableExpression      = 'Variable - \$expression.name : \$expression.type'
                                ConstantExpression      = 'Constant - \$expression.value : \$expression.type'
                                BinaryExpression        = 'Binary - \$expression.text'
                                ClassExpression         = 'Class - \$expression.text'
                                BooleanExpression       = 'Boolean - \$expression.text'
                                ArrayExpression         = 'Array - \$expression.text'
                                ListExpression          = 'List - \$expression.text'
                                TupleExpression         = 'Tuple - \$expression.text'
                                FieldExpression         = 'Field - \$expression.text'
                                PropertyExpression      = 'Property - \$expression.propertyAsString'
                                NotExpression           = 'Not - \$expression.text'
                                CastExpression          = 'Cast - \$expression.text'
                            }
                        }
                    }
                }
            }
        """)        
        customConfig.toProperties()
    }

    static debugPrintTree(node, level = 0) {
        print "  " * level
        println node
        node.children.each {
            printNode(it, level + 1)
        }
    }

    static getCompilationPhasesOptionsHTML() {
        COMPILATION_PHASES_OPTIONS
    }

    static convertToHTML(node, includeSynthetic) {
        def buffer = new StringBuffer()
        populateHTML(node, buffer, 0, includeSynthetic)
        buffer
    }
    
    private static populateHTML(node, buffer, level, includeSynthetic) {
        if(!node) {
            buffer.append("<li>empty<ul></ul></li>")
            return
        }
        def skipRetTuple = skipNode(node, includeSynthetic) 
        if(skipRetTuple[0]) return
        
        def nodeModifier = skipRetTuple[1]
        def nodeModifierStr = "&nbsp;"

        if (nodeModifier != -1) {
            nodeModifierStr = java.lang.reflect.Modifier.toString(nodeModifier)
            if ((nodeModifier & 0x00001000) != 0) nodeModifierStr += " synthetic"
            
            Debug.log nodeModifierStr
        } 

        def str = node.toString()
        str = str.replaceAll("<", "&lt;")
        str = str.replaceAll(">", "&gt;")
    
        if(level) {
            buffer.append("""<li title="$nodeModifierStr">""")
        } else {
            buffer.append("""<li title="$nodeModifierStr" class='liOpen'>""") // level 0 should be open
            if(str == 'root') str = '&nbsp;'
        }
            
        buffer.append(str)
        def childCount = node.children.size()
        if(childCount > 0) {
            buffer.append("<ul>")
            node.children.each {
                populateHTML(it, buffer, level+1, includeSynthetic)
            }
            buffer.append("</ul>")
        }
        buffer.append("</li>")
    }

    static skipNode(node, boolean includeSynthetic) {
        int mod = -1
        boolean skip = false
        def nodeStr = node.toString()
        if(nodeStr.contains("ClassNode - " ) || nodeStr.contains("InnerClassNode - " ) || 
            nodeStr.contains("ConstructorNode - " ) || nodeStr.contains("MethodNode - " ) ||
            nodeStr.contains("FieldNode - " ) || nodeStr.contains("PropertyNode - " )) {
            def modifiersTuple = node?.properties.find{it[0] == 'modifiers'}
            if(modifiersTuple) {
                mod = Integer.parseInt(modifiersTuple[1])
                if(!includeSynthetic && (mod & 0x00001000) != 0)  
                    skip = true
            }
        }
        [skip, mod]
    }
}
