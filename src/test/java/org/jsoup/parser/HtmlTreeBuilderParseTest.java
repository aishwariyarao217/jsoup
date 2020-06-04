package org.jsoup.parser;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InOrder;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.StringReader;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HtmlTreeBuilderParseTest {
    @Test
    public void HtmlTreeBuilderFragmentTransition() {
        HtmlTreeBuilder tb = Mockito.spy(new HtmlTreeBuilder());
        //Tokeniser tokeniser = Mockito.spy(new Tokeniser(new CharacterReader("textarea"), ParseErrorList.noTracking();

        Document document = mock(Document.class);
        when(document.location()).thenReturn("http://www.example.com");

        Element context = mock(Element.class);
        when(context.text()).thenReturn("textarea");

        Tokeniser tokeniser = mock(Tokeniser.class, CALLS_REAL_METHODS);

        tb.parseFragment1("hello", context, document, Parser.htmlParser());

        verify(tokeniser).transition(TokeniserState.Rcdata);
    }
}
