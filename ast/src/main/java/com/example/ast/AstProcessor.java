package com.example.ast;

import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"*"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AstProcessor extends AbstractProcessor {
    private Trees trees;
    private Context context;
    private TreeMaker treeMaker;
    private Name.Table table;
    private static int tally = 0;
    private Messager messager;
    private Names names;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        if (processingEnvironment instanceof JavacProcessingEnvironment) {
            trees = Trees.instance(processingEnvironment);
            context = ((JavacProcessingEnvironment) processingEnvironment).getContext();
            treeMaker = TreeMaker.instance(context);
            table = Names.instance(context).table;
            names = Names.instance(context);
            messager = processingEnvironment.getMessager();
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
//            Set<? extends Element> elements = roundEnv.getRootElements();
//            for (Element each : elements) {
//                if (each.getKind() == ElementKind.CLASS) {
//                    JCTree tree = (JCTree) trees.getTree(each);
//                    TreeTranslator visitor = new Inliner();
//                    tree.accept(visitor);
//                }
//            }
            messager.printMessage(Diagnostic.Kind.WARNING, "AstProcessor ---------- process");
            Set<? extends Element> rootElements = roundEnv.getRootElements();
            for (Element element : rootElements) {
                if (element.getKind() == ElementKind.CLASS) {
                    JCTree tree = (JCTree) trees.getTree(element);
                    tree.accept(new LogTreeTranslator());

                }
            }
        } else
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
                    tally + " assertions inlined.");
        return false;
    }


    private class LogTreeTranslator extends TreeTranslator {
        @Override
        public void visitBlock(JCTree.JCBlock jcBlock) {
            super.visitBlock(jcBlock);
            List<JCTree.JCStatement> statements = jcBlock.getStatements();
            if (statements == null || statements.isEmpty()) {
                return;
            }
            List<JCTree.JCStatement> out = List.nil();  
            for (JCTree.JCStatement jcStatement : statements) {
                messager.printMessage(Diagnostic.Kind.WARNING, "AstProcessor ---------- jcStatement : " + jcStatement);
                if (!jcStatement.toString().contains("Log.")) {
                    out = out.append(jcStatement);
                } else {
                    messager.printMessage(Diagnostic.Kind.WARNING, "AstProcessor ---------- Log : " + jcStatement);
                }
            }
            jcBlock.stats = out;

        }

        @Override
        public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
            super.visitClassDef(jcClassDecl);

        }
    }


    private class Inliner extends TreeTranslator {
        @Override
        public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
            super.visitMethodDef(jcMethodDecl);

        }

        @Override
        public void visitAssert(JCTree.JCAssert jcAssert) {
            super.visitAssert(jcAssert);
            JCTree.JCStatement newNode = makeIfThrowException(jcAssert);
            result = newNode;
            tally++;
        }

        private JCTree.JCStatement makeIfThrowException(JCTree.JCAssert node) {
            // make: if (!(condition) throw new AssertionError(detail);
            List<JCTree.JCExpression> args = node.getDetail() == null ? List.<JCTree.JCExpression>nil()
                    : List.of(node.detail);
            JCTree.JCExpression expr = treeMaker.NewClass(
                    null,
                    null,
                    treeMaker.Ident(table.fromString("AssertionError")),
                    args,
                    null);
            return treeMaker.If(
                    treeMaker.Unary(JCTree.Tag.NOT, node.cond),
                    treeMaker.Throw(expr),
                    null);
        }

    }
}
