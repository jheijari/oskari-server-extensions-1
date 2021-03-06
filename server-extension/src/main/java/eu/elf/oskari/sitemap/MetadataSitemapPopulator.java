package eu.elf.oskari.sitemap;

import fi.mml.portti.service.search.*;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.search.channel.MetadataCatalogueChannelSearchService;
import fi.nls.oskari.util.PropertyUtil;
import fi.nls.oskari.util.ServiceFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RLINKALA on 24.2.2016.
 */
public class MetadataSitemapPopulator {

    private final static Logger LOG = LogFactory.getLogger(MetadataSitemapPopulator.class);

    private static String DOMAIN_URL = "oskari.domain";

    private static final SearchService service = ServiceFactory.getSearchService();
    private static final OMFactory OM_FACTORY = OMAbstractFactory.getOMFactory();



    public MetadataSitemapPopulator() {
    }

    public String buildSiteMap() throws Exception {

        OMElement urlElement = null;
        OMElement locElement = null;
        final String domain = PropertyUtil.get(DOMAIN_URL, "http://localhost:8080");
        final OMElement root = OM_FACTORY.createOMElement("urlset", null);
        final OMAttribute xmlns = OM_FACTORY.createOMAttribute("xmlns", null, "http://www.sitemaps.org/schemas/sitemap/0.9");

        root.addAttribute(xmlns);

        List<String> metadataUuids = getUuids();

        if (metadataUuids == null || metadataUuids.isEmpty())
            throw new Exception("No uuids found... The service might be unable to answer");


        LOG.debug("metadataUuids size: " + metadataUuids.size());

        for (String metadataId : metadataUuids) {
            urlElement = OM_FACTORY.createOMElement("url", null);
            locElement = OM_FACTORY.createOMElement("loc", null);
            locElement.setText(domain + "/?metadata=" + metadataId);
            urlElement.addChild(locElement);
            root.addChild(urlElement);
        }

        return root.toString();
    }

    private List<String> getUuids() {

        List<String> uuids = new ArrayList<>();

        final SearchCriteria sc = new SearchCriteria();
        final String language = "en";
        sc.setLocale(language);
        sc.setSRS("EPSG:3857");
        sc.setSearchString("*");

        sc.addChannel(MetadataCatalogueChannelSearchService.ID);
        final Query query = service.doSearch(sc);
        final ChannelSearchResult searchResult = query.findResult(MetadataCatalogueChannelSearchService.ID);

        LOG.debug("done search... now creating a list");

        for (SearchResultItem item : searchResult.getSearchResultItems()) {
            if (item.getResourceId() != null) {
                LOG.debug("Adding id: " + item.getResourceId());
                uuids.add(item.getResourceId());
            }
        }
        return uuids;
    }
}
