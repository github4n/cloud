import org.apache.http.HttpHeaders
import org.springframework.cloud.contract.spec.Contract
import org.springframework.http.MediaType

Contract.make {
    request {
        method GET()
        url "/devCatalog/getDeviceCatalog"
//        url('/devType/findAllDeviceTypeList') {
//            queryParameters {
//                parameter("name", "zhangsan")
//            }
//        }

    }
    response {
        status 200
        headers {
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
        }
        body("""
          [{"id":"1","parentId":null,"name":"Starter Kits","description":"Starter Kits","tenantId":0},{"id":"2","parentId":null,"name":"Surveilance","description":"IPCamera","tenantId":-1},{"id":"3","parentId":null,"name":"Hub","description":"Siren Hub\\\\Multi-protocol hub\\\\Zigbee Hub","tenantId":-1},{"id":"4","parentId":null,"name":"Lighting","description":"Light\\\\Remote","tenantId":-1},{"id":"5","parentId":null,"name":"Security","description":"Door&window sensor\\\\Motion Sensor\\\\Water Leak Sensor\\\\Keypad&Keyfob","tenantId":-1},{"id":"6","parentId":null,"name":"Energy","description":"plug\\\\Temperature&Humidity","tenantId":-1}]
          """)
    }
}