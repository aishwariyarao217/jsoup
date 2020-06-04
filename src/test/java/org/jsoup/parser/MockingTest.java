package org.jsoup.parser;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InOrder;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Document;

import java.io.StringReader;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MockingTest {
    @Test
    public void htmlTreeBuilderTransitionTest1() {
        HtmlTreeBuilder tb = spy(new HtmlTreeBuilder());

        Document document = mock(Document.class);
        when(document.location()).thenReturn("http://www.example.com");

        Tokeniser tokeniser = mock(Tokeniser.class);
        when(tokeniser.read()).thenReturn(new Token.Comment(), new Token.StartTag(), new Token.EOF());

        tb.parse(new StringReader("hello"), document, Parser.htmlParser(), tokeniser);

        verify(tb).transition(HtmlTreeBuilderState.BeforeHtml);
        verify(tb).transition(HtmlTreeBuilderState.BeforeHead);
        verify(tb).transition(HtmlTreeBuilderState.InHead);
    }
}
