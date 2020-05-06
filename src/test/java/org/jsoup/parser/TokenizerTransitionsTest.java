package org.jsoup.parser;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InOrder;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TokenizerTransitionsTest {

    public Tokeniser setup(String input) {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(input), ParseErrorList.noTracking());
        return Mockito.spy(tokeniser);
    }

    public Tokeniser setup(String input, TokeniserState state) {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(input), ParseErrorList.noTracking());
        tokeniser.transition(state);
        return Mockito.spy(tokeniser);
    }
   
    @Test
    public void testTokeniserNormalTag() {
        Tokeniser tokeniser = setup("<p>");
        InOrder inOrder = Mockito.inOrder(tokeniser);
        tokeniser.read();
        inOrder.verify(tokeniser, times(1)).advanceTransition(TokeniserState.TagOpen);
        inOrder.verify(tokeniser, times(1)).transition(TokeniserState.TagName);
    }
    
    @Test
    public void testMissingTagBracket() {
        Tokeniser tokeniser = setup("p>", TokeniserState.TagOpen);
        tokeniser.read();
        Mockito.verify(tokeniser).transition(TokeniserState.TagName);
    }
    
  @Test
    public void testNoTextTag() {
        Tokeniser tokeniser = setup("</>", TokeniserState.Data);
        InOrder inOrder = Mockito.inOrder(tokeniser);
        tokeniser.read();
        inOrder.verify(tokeniser).advanceTransition(TokeniserState.TagOpen);
        inOrder.verify(tokeniser).advanceTransition(TokeniserState.EndTagOpen);
        inOrder.verify(tokeniser).advanceTransition(TokeniserState.Data);
    }
   
    @Test
    public void testSelfClosingTag() {
        Tokeniser tokeniser = setup("<input/>", TokeniserState.Data);
        InOrder inOrder = Mockito.inOrder(tokeniser);
        tokeniser.read();
        inOrder.verify(tokeniser).advanceTransition(TokeniserState.TagOpen);
        inOrder.verify(tokeniser).transition(TokeniserState.TagName);
        inOrder.verify(tokeniser).transition(TokeniserState.SelfClosingStartTag);
        inOrder.verify(tokeniser).transition(TokeniserState.Data);
    }
    
        @Test
    public void testAfterAttributeName() {
        Tokeniser tokeniser = setup("color", TokeniserState.AfterAttributeName);
        InOrder inOrder = Mockito.inOrder(tokeniser);
        tokeniser.createTagPending(true);
        tokeniser.read();
        inOrder.verify(tokeniser).transition(TokeniserState.AttributeName);
    }
}
