//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.auth.oauth2;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpBackOffIOExceptionHandler;
import com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandler;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandler.BackOffRequired;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.json.webtoken.JsonWebSignature.Header;
import com.google.api.client.json.webtoken.JsonWebToken.Payload;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Joiner;
import com.google.api.client.util.PemReader;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.SecurityUtils;
import com.google.api.client.util.PemReader.Section;
import com.google.auth.ServiceAccountSigner;
import com.google.auth.ServiceAccountSigner.SigningException;
import com.google.auth.http.HttpTransportFactory;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.OAuth2Utils;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.Date;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class ServiceAccountCredentials extends GoogleCredentials implements ServiceAccountSigner {
    private static final Logger logger = LoggerFactory.getLogger(ServiceAccountCredentials.class);

    private static final long serialVersionUID = 7807543542681217978L;
    private static final String GRANT_TYPE = "urn:ietf:params:oauth:grant-type:jwt-bearer";
    private static final String PARSE_ERROR_PREFIX = "Error parsing token refresh response. ";
    private final String clientId;
    private final String clientEmail;
    private final PrivateKey privateKey;
    private final String privateKeyId;
    private final String serviceAccountUser;
    private final String projectId;
    private final String transportFactoryClassName;
    private final URI tokenServerUri;
    private final Collection<String> scopes;
    private transient HttpTransportFactory transportFactory;

    /** @deprecated */
    @Deprecated
    public ServiceAccountCredentials(String clientId, String clientEmail, PrivateKey privateKey, String privateKeyId, Collection<String> scopes) {
        this(clientId, clientEmail, privateKey, privateKeyId, scopes, (HttpTransportFactory)null, (URI)null, (String)null, (String)null);
    }

    /** @deprecated */
    @Deprecated
    public ServiceAccountCredentials(String clientId, String clientEmail, PrivateKey privateKey, String privateKeyId, Collection<String> scopes, HttpTransportFactory transportFactory, URI tokenServerUri) {
        this(clientId, clientEmail, privateKey, privateKeyId, scopes, transportFactory, tokenServerUri, (String)null, (String)null);
    }

    ServiceAccountCredentials(String clientId, String clientEmail, PrivateKey privateKey, String privateKeyId, Collection<String> scopes, HttpTransportFactory transportFactory, URI tokenServerUri, String serviceAccountUser, String projectId) {
        this.clientId = clientId;
        this.clientEmail = (String)Preconditions.checkNotNull(clientEmail);
        this.privateKey = (PrivateKey)Preconditions.checkNotNull(privateKey);
        this.privateKeyId = privateKeyId;
        this.scopes = scopes == null?ImmutableSet.of():ImmutableSet.copyOf(scopes);
        this.transportFactory = (HttpTransportFactory)MoreObjects.firstNonNull(transportFactory, getFromServiceLoader(HttpTransportFactory.class, OAuth2Utils.HTTP_TRANSPORT_FACTORY));
        this.transportFactoryClassName = this.transportFactory.getClass().getName();
        this.tokenServerUri = tokenServerUri == null?OAuth2Utils.TOKEN_SERVER_URI:tokenServerUri;
        this.serviceAccountUser = serviceAccountUser;
        this.projectId = projectId;
    }

    static ServiceAccountCredentials fromJson(Map<String, Object> json, HttpTransportFactory transportFactory) throws IOException {
        String clientId = (String)json.get("client_id");
        String clientEmail = (String)json.get("client_email");
        String privateKeyPkcs8 = (String)json.get("private_key");
        String privateKeyId = (String)json.get("private_key_id");
        String projectId = (String)json.get("project_id");
        String tokenServerUriStringFromCreds = (String)json.get("token_uri");
        URI tokenServerUriFromCreds = null;

        try {
            if(tokenServerUriStringFromCreds != null) {
                tokenServerUriFromCreds = new URI(tokenServerUriStringFromCreds);
            }
        } catch (URISyntaxException var10) {
            throw new IOException("Token server URI specified in \'token_uri\' could not be parsed.");
        }

        if(clientId != null && clientEmail != null && privateKeyPkcs8 != null && privateKeyId != null) {
            logger.debug("***** fromJson, before replaceAll, privateKeyPkcs8 = {}", privateKeyPkcs8);
            if (privateKeyPkcs8.contains("\\n")) {
                logger.debug("****************** privateKeyPkcs8 contains \\n......");
                privateKeyPkcs8 = privateKeyPkcs8.replaceAll("\\\\n", "\n");
            } else {
                logger.debug("****************** privateKeyPkcs8 don't contains \\n......");
            }
            logger.debug("***** fromJson, after replaceAll, privateKeyPkcs8 = {}", privateKeyPkcs8);

            return fromPkcs8(clientId, clientEmail, privateKeyPkcs8, privateKeyId, (Collection)null, transportFactory, tokenServerUriFromCreds, (String)null, projectId);
        } else {
            throw new IOException("Error reading service account credential from JSON, expecting  \'client_id\', \'client_email\', \'private_key\' and \'private_key_id\'.");
        }
    }

    public static ServiceAccountCredentials fromPkcs8(String clientId, String clientEmail, String privateKeyPkcs8, String privateKeyId, Collection<String> scopes) throws IOException {
        return fromPkcs8(clientId, clientEmail, privateKeyPkcs8, privateKeyId, scopes, (HttpTransportFactory)null, (URI)null, (String)null);
    }

    public static ServiceAccountCredentials fromPkcs8(String clientId, String clientEmail, String privateKeyPkcs8, String privateKeyId, Collection<String> scopes, HttpTransportFactory transportFactory, URI tokenServerUri) throws IOException {
        return fromPkcs8(clientId, clientEmail, privateKeyPkcs8, privateKeyId, scopes, transportFactory, tokenServerUri, (String)null);
    }

    public static ServiceAccountCredentials fromPkcs8(String clientId, String clientEmail, String privateKeyPkcs8, String privateKeyId, Collection<String> scopes, HttpTransportFactory transportFactory, URI tokenServerUri, String serviceAccountUser) throws IOException {
        return fromPkcs8(clientId, clientEmail, privateKeyPkcs8, privateKeyId, scopes, transportFactory, tokenServerUri, serviceAccountUser, (String)null);
    }

    static ServiceAccountCredentials fromPkcs8(String clientId, String clientEmail, String privateKeyPkcs8, String privateKeyId, Collection<String> scopes, HttpTransportFactory transportFactory, URI tokenServerUri, String serviceAccountUser, String projectId) throws IOException {
        PrivateKey privateKey = privateKeyFromPkcs8(privateKeyPkcs8);
        return new ServiceAccountCredentials(clientId, clientEmail, privateKey, privateKeyId, scopes, transportFactory, tokenServerUri, serviceAccountUser, projectId);
    }

    static PrivateKey privateKeyFromPkcs8(String privateKeyPkcs8) throws IOException {
        StringReader reader = new StringReader(privateKeyPkcs8);
        Section section = PemReader.readFirstSectionAndClose(reader, "PRIVATE KEY");
        if(section == null) {
            throw new IOException("Invalid PKCS#8 data.");
        } else {
            byte[] bytes = section.getBase64DecodedBytes();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);

            try {
                KeyFactory exception = SecurityUtils.getRsaKeyFactory();
                return exception.generatePrivate(keySpec);
            } catch (InvalidKeySpecException | NoSuchAlgorithmException var7) {
                throw new IOException("Unexpected exception reading PKCS#8 data", var7);
            }
        }
    }

    public static ServiceAccountCredentials fromStream(InputStream credentialsStream) throws IOException {
        return fromStream(credentialsStream, OAuth2Utils.HTTP_TRANSPORT_FACTORY);
    }

    public static ServiceAccountCredentials fromStream(InputStream credentialsStream, HttpTransportFactory transportFactory) throws IOException {
        Preconditions.checkNotNull(credentialsStream);
        Preconditions.checkNotNull(transportFactory);
        JsonFactory jsonFactory = OAuth2Utils.JSON_FACTORY;
        JsonObjectParser parser = new JsonObjectParser(jsonFactory);
        GenericJson fileContents = (GenericJson)parser.parseAndClose(credentialsStream, OAuth2Utils.UTF_8, GenericJson.class);
        String fileType = (String)fileContents.get("type");
        if(fileType == null) {
            throw new IOException("Error reading credentials from stream, \'type\' field not specified.");
        } else if("service_account".equals(fileType)) {
            return fromJson(fileContents, transportFactory);
        } else {
            throw new IOException(String.format("Error reading credentials from stream, \'type\' value \'%s\' not recognized. Expecting \'%s\'.", new Object[]{fileType, "service_account"}));
        }
    }

    public AccessToken refreshAccessToken() throws IOException {
        if(this.createScopedRequired()) {
            throw new IOException("Scopes not configured for service account. Scoped should be specified by calling createScoped or passing scopes to constructor.");
        } else {
            JsonFactory jsonFactory = OAuth2Utils.JSON_FACTORY;
            long currentTime = this.clock.currentTimeMillis();
            String assertion = this.createAssertion(jsonFactory, currentTime, this.tokenServerUri.toString());
            GenericData tokenRequest = new GenericData();
            tokenRequest.set("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer");
            tokenRequest.set("assertion", assertion);
            UrlEncodedContent content = new UrlEncodedContent(tokenRequest);
            HttpRequestFactory requestFactory = this.transportFactory.create().createRequestFactory();
            HttpRequest request = requestFactory.buildPostRequest(new GenericUrl(this.tokenServerUri), content);
            request.setParser(new JsonObjectParser(jsonFactory));
            request.setIOExceptionHandler(new HttpBackOffIOExceptionHandler(new ExponentialBackOff()));
            request.setUnsuccessfulResponseHandler((new HttpBackOffUnsuccessfulResponseHandler(new ExponentialBackOff())).setBackOffRequired(new BackOffRequired() {
                public boolean isRequired(HttpResponse response) {
                    int code = response.getStatusCode();
                    return code / 100 == 5 || code == 403;
                }
            }));

            HttpResponse response;
            try {
                response = request.execute();
            } catch (IOException var15) {
                throw new IOException("Error getting access token for service account: ", var15);
            }

            GenericData responseData = (GenericData)response.parseAs(GenericData.class);
            String accessToken = OAuth2Utils.validateString(responseData, "access_token", "Error parsing token refresh response. ");
            int expiresInSeconds = OAuth2Utils.validateInt32(responseData, "expires_in", "Error parsing token refresh response. ");
            long expiresAtMilliseconds = this.clock.currentTimeMillis() + (long)expiresInSeconds * 1000L;
            return new AccessToken(accessToken, new Date(expiresAtMilliseconds));
        }
    }

    public boolean createScopedRequired() {
        return this.scopes.isEmpty();
    }

    public GoogleCredentials createScoped(Collection<String> newScopes) {
        return new ServiceAccountCredentials(this.clientId, this.clientEmail, this.privateKey, this.privateKeyId, newScopes, this.transportFactory, this.tokenServerUri, this.serviceAccountUser, this.projectId);
    }

    public GoogleCredentials createDelegated(String user) {
        return new ServiceAccountCredentials(this.clientId, this.clientEmail, this.privateKey, this.privateKeyId, this.scopes, this.transportFactory, this.tokenServerUri, user, this.projectId);
    }

    public final String getClientId() {
        return this.clientId;
    }

    public final String getClientEmail() {
        return this.clientEmail;
    }

    public final PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public final String getPrivateKeyId() {
        return this.privateKeyId;
    }

    public final Collection<String> getScopes() {
        return this.scopes;
    }

    public final String getServiceAccountUser() {
        return this.serviceAccountUser;
    }

    public final String getProjectId() {
        return this.projectId;
    }

    public final URI getTokenServerUri() {
        return this.tokenServerUri;
    }

    public String getAccount() {
        return this.getClientEmail();
    }

    public byte[] sign(byte[] toSign) {
        try {
            Signature ex = Signature.getInstance("SHA256withRSA");
            ex.initSign(this.getPrivateKey());
            ex.update(toSign);
            return ex.sign();
        } catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException var3) {
            throw new SigningException("Failed to sign the provided bytes", var3);
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.clientId, this.clientEmail, this.privateKey, this.privateKeyId, this.transportFactoryClassName, this.tokenServerUri, this.scopes});
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("clientId", this.clientId).add("clientEmail", this.clientEmail).add("privateKeyId", this.privateKeyId).add("transportFactoryClassName", this.transportFactoryClassName).add("tokenServerUri", this.tokenServerUri).add("scopes", this.scopes).add("serviceAccountUser", this.serviceAccountUser).toString();
    }

    public boolean equals(Object obj) {
        if(!(obj instanceof ServiceAccountCredentials)) {
            return false;
        } else {
            ServiceAccountCredentials other = (ServiceAccountCredentials)obj;
            return Objects.equals(this.clientId, other.clientId) && Objects.equals(this.clientEmail, other.clientEmail) && Objects.equals(this.privateKey, other.privateKey) && Objects.equals(this.privateKeyId, other.privateKeyId) && Objects.equals(this.transportFactoryClassName, other.transportFactoryClassName) && Objects.equals(this.tokenServerUri, other.tokenServerUri) && Objects.equals(this.scopes, other.scopes);
        }
    }

    String createAssertion(JsonFactory jsonFactory, long currentTime, String audience) throws IOException {
        Header header = new Header();
        header.setAlgorithm("RS256");
        header.setType("JWT");
        header.setKeyId(this.privateKeyId);
        Payload payload = new Payload();
        payload.setIssuer(this.clientEmail);
        payload.setIssuedAtTimeSeconds(Long.valueOf(currentTime / 1000L));
        payload.setExpirationTimeSeconds(Long.valueOf(currentTime / 1000L + 3600L));
        payload.setSubject(this.serviceAccountUser);
        payload.put("scope", Joiner.on(' ').join(this.scopes));
        if(audience == null) {
            payload.setAudience(OAuth2Utils.TOKEN_SERVER_URI.toString());
        } else {
            payload.setAudience(audience);
        }

        try {
            String assertion = JsonWebSignature.signUsingRsaSha256(this.privateKey, jsonFactory, header, payload);
            return assertion;
        } catch (GeneralSecurityException var9) {
            throw new IOException("Error signing service account access token request with private key.", var9);
        }
    }

    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        this.transportFactory = (HttpTransportFactory)newInstance(this.transportFactoryClassName);
    }

    public static ServiceAccountCredentials.Builder newBuilder() {
        return new ServiceAccountCredentials.Builder();
    }

    public ServiceAccountCredentials.Builder toBuilder() {
        return new ServiceAccountCredentials.Builder(this);
    }

    public static class Builder extends com.google.auth.oauth2.GoogleCredentials.Builder {
        private String clientId;
        private String clientEmail;
        private PrivateKey privateKey;
        private String privateKeyId;
        private String serviceAccountUser;
        private String projectId;
        private URI tokenServerUri;
        private Collection<String> scopes;
        private HttpTransportFactory transportFactory;

        protected Builder() {
        }

        protected Builder(ServiceAccountCredentials credentials) {
            this.clientId = credentials.clientId;
            this.clientEmail = credentials.clientEmail;
            this.privateKey = credentials.privateKey;
            this.privateKeyId = credentials.privateKeyId;
            this.scopes = credentials.scopes;
            this.transportFactory = credentials.transportFactory;
            this.tokenServerUri = credentials.tokenServerUri;
            this.serviceAccountUser = credentials.serviceAccountUser;
            this.projectId = credentials.projectId;
        }

        public ServiceAccountCredentials.Builder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public ServiceAccountCredentials.Builder setClientEmail(String clientEmail) {
            this.clientEmail = clientEmail;
            return this;
        }

        public ServiceAccountCredentials.Builder setPrivateKey(PrivateKey privateKey) {
            this.privateKey = privateKey;
            return this;
        }

        public ServiceAccountCredentials.Builder setPrivateKeyId(String privateKeyId) {
            this.privateKeyId = privateKeyId;
            return this;
        }

        public ServiceAccountCredentials.Builder setScopes(Collection<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        public ServiceAccountCredentials.Builder setServiceAccountUser(String serviceAccountUser) {
            this.serviceAccountUser = serviceAccountUser;
            return this;
        }

        public ServiceAccountCredentials.Builder setProjectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public ServiceAccountCredentials.Builder setTokenServerUri(URI tokenServerUri) {
            this.tokenServerUri = tokenServerUri;
            return this;
        }

        public ServiceAccountCredentials.Builder setHttpTransportFactory(HttpTransportFactory transportFactory) {
            this.transportFactory = transportFactory;
            return this;
        }

        public String getClientId() {
            return this.clientId;
        }

        public String getClientEmail() {
            return this.clientEmail;
        }

        public PrivateKey getPrivateKey() {
            return this.privateKey;
        }

        public String getPrivateKeyId() {
            return this.privateKeyId;
        }

        public Collection<String> getScopes() {
            return this.scopes;
        }

        public String getServiceAccountUser() {
            return this.serviceAccountUser;
        }

        public String getProjectId() {
            return this.projectId;
        }

        public URI getTokenServerUri() {
            return this.tokenServerUri;
        }

        public HttpTransportFactory getHttpTransportFactory() {
            return this.transportFactory;
        }

        public ServiceAccountCredentials build() {
            return new ServiceAccountCredentials(this.clientId, this.clientEmail, this.privateKey, this.privateKeyId, this.scopes, this.transportFactory, this.tokenServerUri, this.serviceAccountUser, this.projectId);
        }
    }
}
