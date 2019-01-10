import org.apache.http.HttpHeaders
import org.springframework.cloud.contract.spec.Contract
import org.springframework.http.MediaType

Contract.make {
    request {
        method GET()
        url "/devType/findAllDeviceTypeList"
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
          [{"id":41,"name":"Siren_Hub","code":"Siren_Hub","img":"fefa4a8862514d47a4c1c2df5257f9d6","whetherSoc":1},{"id":42,"name":"Hub","code":"Hub","img":"23d865b819924551964b41b32d73970f","whetherSoc":1},{"id":43,"name":"Lights","code":"Lights","img":"4475441760f04948a6fe0da9d2daa656","whetherSoc":1}]
          """)
    }
}