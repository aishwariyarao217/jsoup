package org.jsoup.parser;
import org.jsoup.nodes.*;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StructuralTesting {
    private HtmlTreeBuilder createTestTreeBuilder() {
        HtmlTreeBuilder tree = new HtmlTreeBuilder();
        tree.initialiseParse(new StringReader(" "), " ", new Parser(tree));
        tree.parser.setTrackErrors(1);
        return tree;
    }

    @Test
    public void testCommentInColumnGroup() {
        HtmlTreeBuilder tree = createTestTreeBuilder();
        tree.process(new Token.Comment(), HtmlTreeBuilderState.InColumnGroup);
        assertEquals(tree.getDocument().childNode(0).nodeName(), "#comment");
    }

    @Test
    public void testDoctypeInColumnGroup() {
        HtmlTreeBuilder tree = createTestTreeBuilder();
        tree.process(new Token.Doctype(), HtmlTreeBuilderState.InColumnGroup);
        assertEquals(tree.parser.getErrors().size(), 1);
    }

    @Test
    public void testStartTagInSelectInTable() {
        HtmlTreeBuilder tree = createTestTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("caption");
        tree.process(startTag, HtmlTreeBuilderState.InSelectInTable);
        assertEquals(tree.parser.getErrors().size(), 1);
    }
    @Test
    public void testFramesetStartTagInFrameset() {
        HtmlTreeBuilder tb = createTestTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("frameset");
        tb.process(startTag, HtmlTreeBuilderState.InFrameset);
        assertEquals(tb.pop().normalName(), "frameset");
    }

    @Test
    public void testDefaultStartTagInFrameset() {
        HtmlTreeBuilder tb = createTestTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("world");
        tb.process(startTag, HtmlTreeBuilderState.InFrameset);
        assertEquals(tb.parser.getErrors().size(), 1);
    }

    @Test
    public void testEOFInFrameset() {
        HtmlTreeBuilder tb = createTestTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("html");
        tb.process(startTag);
        tb.process(new Token.EOF(), HtmlTreeBuilderState.InFrameset);
        assertEquals(tb.parser.getErrors().size(), 0);
    }

/*   @Test
    public void testEndTagInFrameset() {
        HtmlTreeBuilder tb = createTestTreeBuilder();
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("frameset");
        tb.process(endTag, HtmlTreeBuilderState.InFrameset);
        assertEquals(tb.pop().normalName(), "frameset");
    }
*/
    @Test
    public void testPlainTextParseFragment(){
        String html = "<ul><li>Item 1</li></ul><p>Paragraph 1</p><p>Paragraph 2</p>";
        Element context =new Element("plaintext");
        List<Node> nodes = Parser.parseFragment(html, context, "http://example.com/");
        assertEquals(3,nodes.size());
    }

    @Test
    public void testScriptDataParseFragment(){
        String html = "<script>int i=1, j = 2;</script>";
        Element context =new Element("script");
        List<Node> nodes = Parser.parseFragment(html, context, "http://example.com/");
        assertEquals(1,nodes.size());
    }

    @Test
    public void testNoScriptParseFragment(){
        String html = "<script>\n" +
                "document.write(\"Writing to File\")\n" +
                "</script>\n" +
                "<noscript>Your browser does not support JavaScript!</noscript>";
        Element context =new Element("noscript");
        List<Node> nodes = Parser.parseFragment(html, context, "http://example.com/");
        assertEquals(3,nodes.size());
    }

    @Test
    public void testSelectResetInsertionMode(){
        String html = " <select id=\"cars\">\n" +
                "  <option value=\"volvo\">Volvo</option>\n" +
                "  <option value=\"saab\">Saab</option>\n" +
                "  <option value=\"mercedes\">Mercedes</option>\n" +
                "  <option value=\"audi\">Audi</option>\n" +
                "</select>";
        Element context =new Element("select");
        List<Node> nodes = Parser.parseFragment(html, context, "http://example.com/");
        assertEquals(10,nodes.size());
    }

    @Test
    public void testTableRowResetInsertionMode(){
        String html = "<tr><td>1</td><td>Software Testing and Debugging</td></tr>";
        Element context =new Element("tr");
        List<Node> nodes = Parser.parseFragment(html, context, "http://example.com/");
        assertEquals(2,nodes.size());
    }

    @Test
    public void testCaptionResetInsertionMode(){
        String html = "<table>\n" +
                "  <caption>Monthly savings</caption>\n" +
                "  <tr>\n" +
                "    <th>Month</th>\n" +
                "    <th>Savings</th>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>January</td>\n" +
                "    <td>$100</td>\n" +
                "  </tr>\n" +
                "</table>";
        Element context =new Element("caption");
        List<Node> nodes = Parser.parseFragment(html, context, "http://example.com/");
        assertEquals(1,nodes.size());
    }

    @Test
    public void testColumnGroupResetInsertionMode(){
        String html = " <table>\n" +
                "  <colgroup>\n" +
                "    <col span=\"3\" style=\"background-color:blue\">\n" +
                "    <col style=\"background-color:green\">\n" +
                "  </colgroup>\n" +
                "  <tr>\n" +
                "    <th>Serial Number</th>\n" +
                "    <th>Item</th>\n" +
                "    <th>Quantity</th>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>1</td>\n" +
                "    <td>Pizza</td>\n" +
                "    <td>$5</td>\n" +
                "  </tr>\n" +
                "</table>";
        Element context =new Element("colgroup");
        List<Node> nodes = Parser.parseFragment(html, context, "http://example.com/");
        assertEquals(18,nodes.size());
    }

    @Test
    public void testTableResetInsertionMode(){
        String html = "<table>\n" +
                "  <caption>Inventory</caption>\n" +
                "  <tr>\n" +
                "    <th>Item</th>\n" +
                "    <th>Quantity</th>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Cheese</td>\n" +
                "    <td>5</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Bread</th>\n" +
                "    <td>10</th>\n" +
                "  </tr>\n" +
                "</table>";
        Element context =new Element("table");
        List<Node> nodes = Parser.parseFragment(html, context, "http://example.com/");
        assertEquals(4,nodes.size());
    }

    @Test
    public void testFramesetResetInsertionMode(){
        String html = "<frameset cols=\"50%,*, 100%\">\n" +
                "  <frame src=\"frame_1.htm\">\n" +
                "  <frame src=\"frame_2.htm\">\n" +
                "  <frame src=\"frame_3.htm\">\n" +
                "  <frame src=\"frame_4.htm\">\n" +
                "  <frame src=\"frame_5.htm\">\n" +
                "</frameset>";
        Element context =new Element("frameset");
        List<Node> nodes = Parser.parseFragment(html, context, "http://example.com/");
        assertEquals(1,nodes.size());
    }

    @Test
    public void testHTMLResetInsertionMode() {
        String html = "<html>\n" +
                "<head>\n" +
                "<title>My First HTML Document</title>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "<p>This is a paragraph\n" +
                "<p>This is another paragraph\n" +
                "</body>\n" +
                "\n" +
                "</html>";
        Element context = new Element("html");
        List<Node> nodes = Parser.parseFragment(html, context, "http://example.com/");
        assertEquals(4, nodes.size());
    }
}