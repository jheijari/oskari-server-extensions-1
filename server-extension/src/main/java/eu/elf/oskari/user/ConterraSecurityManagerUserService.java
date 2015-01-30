package eu.elf.oskari.user;

import fi.nls.oskari.domain.Role;
import fi.nls.oskari.domain.User;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.service.ServiceException;
import fi.nls.oskari.user.DatabaseUserService;
import fi.nls.oskari.util.IOHelper;
import fi.nls.oskari.util.PropertyUtil;

import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by SMAKINEN on 27.1.2015.
 */
public class ConterraSecurityManagerUserService extends DatabaseUserService {

    private static Logger log = LogFactory.getLogger(ConterraSecurityManagerUserService.class);
    private String serviceURL = "";
    private String payloadTemplate = "";

    private static final String PARAM_METHOD = "METHOD";
    private static final String PARAM_METHOD_VALUE = "urn:opengeospatial:authNMethod:OWS:1.0:password";
    private static final String PARAM_REQUEST = "REQUEST";
    private static final String PARAM_REQUEST_VALUE = "GetSAMLResponse";
    private static final String PARAM_CREDENTIALS = "CREDENTIALS";

    private final UserXMLMapping mapping = new UserXMLMapping();

    @Override
    public void init() throws ServiceException {
        super.init();
        // LinkedHashMap handles order so we get the empty PARAM_CREDENTIALS as the last param
        // this way we can just do payloadTemplate + <credentials value>
        final Map<String, String> params = new LinkedHashMap<String, String>();
        params.put(PARAM_METHOD, PARAM_METHOD_VALUE);
        params.put(PARAM_REQUEST, PARAM_REQUEST_VALUE);
        params.put(PARAM_CREDENTIALS, "");
        payloadTemplate = IOHelper.getParams(params);
        try {
            // get the service url from properties
            serviceURL = PropertyUtil.getNecessary("eu.elf.oskari.user.ConterraSecurityManagerUserService.url");
        }
        catch (Exception ex) {
            throw new ServiceException("Couldn't get url for login service: " + ex.getMessage(), ex);
        }
    }

    @Override
    public User login(String username, String pass) throws ServiceException {
        try {
            HttpURLConnection conn = IOHelper.getConnection(serviceURL);
            IOHelper.setContentType(conn, IOHelper.CONTENTTYPE_FORM_URLENCODED);
            final String payload = payloadTemplate + IOHelper.encode64(username) + "," + IOHelper.encode64(pass);
            IOHelper.writeToConnection(conn, payload);
            final String response = IOHelper.readString(conn);
            if(response.isEmpty()) {
                throw new ServiceException("Couldn't get response from server " + serviceURL + " with payload:\n" + payload);
            }
            User user = parseResponse(IOHelper.decode64(response));
            if(user != null) {
                // save to database
                user = updateOrAddUser(user);
            }

            return user;
        }
        catch (Exception ex) {
            if(ex instanceof ServiceException) {
                throw (ServiceException) ex;
            }
            log.error(ex, "Error logging in. URL:", serviceURL, ".User:", username);
        }
        return null;
    }


    public User parseResponse(final String response) {
        if(response == null) {
            return null;
        }

        log.debug("Got response:\n",response);
        return mapping.parse(response);
    }
}
