import groovyx.net.http.HTTPBuilder
import org.apache.commons.io.IOUtils

import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.Method.GET
import static org.assertj.core.api.Assertions.assertThat

def http = new HTTPBuilder('http://localhost:8080')
http.request(GET, TEXT) { req ->
    response.success = { resp, reader ->
        assert resp.status == 200
        def data = IOUtils.toString(reader)
        assertThat(data)
                .contains("my jade template")
                .contains("Hello Vert.x Web")
    }

    // called only for a 404 (not found) status code:
    response.'404' = { resp ->
        assert false
    }
}

return true

