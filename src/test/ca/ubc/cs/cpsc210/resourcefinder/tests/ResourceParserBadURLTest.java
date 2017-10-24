package ca.ubc.cs.cpsc210.resourcefinder.tests;

import ca.ubc.cs.cpsc210.resourcefinder.model.ContactInfo;
import ca.ubc.cs.cpsc210.resourcefinder.model.Resource;
import ca.ubc.cs.cpsc210.resourcefinder.model.ResourceRegistry;
import ca.ubc.cs.cpsc210.resourcefinder.parser.IResourceParser;
import ca.ubc.cs.cpsc210.resourcefinder.parser.ResourceParsingException;
import ca.ubc.cs.cpsc210.resourcefinder.parser.XMLResourceParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.IIOException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.fail;

// Unit tests for XMLResourceParser class
public class ResourceParserBadURLTest {
    // TODO: test the parser when a resource has a bad URL (e.g., data/resourceWithBadURL.xml)
    private static final String FILE_NAME = "./data/resourceWithBadURL.xml";
    private ResourceRegistry registry;
    private IResourceParser resourceParser;

    @BeforeEach
    public void runBefore() {
        resourceParser = new XMLResourceParser(FILE_NAME);

    }

    @Test
    public void testBadURL() {
        try {
            registry = resourceParser.parse();
            fail("Exception ResourceParsingException should have been thrown");
        } catch (ResourceParsingException e) {
            //expected

        } catch (IOException e) {
            e.printStackTrace();
            fail("Wrong Exception thrown");
        }
    }
}