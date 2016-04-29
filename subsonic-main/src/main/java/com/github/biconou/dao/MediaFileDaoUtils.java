/**
 * Paquet de définition
 **/
package com.github.biconou.dao;

import java.io.IOException;
import java.util.List;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import net.sourceforge.subsonic.domain.MediaFile;

/**
 * Description: Merci de donner une description du service rendu par cette classe
 */
public class MediaFileDaoUtils {

  protected static SearchResponse searchMediaFileByPath(ElasticSearchClient client,String path) {
    String jsonSearch = "" +
      "{" +
      "    \"constant_score\" : {" +
      "        \"filter\" : {" +
      "            \"term\" : {" +
      "                \"path\" : \""+path+"\"" +
      "            }" +
      "        }" +
      "    }" +
      "}";


    return client.getClient().prepareSearch(ElasticSearchClient.SUBSONIC_MEDIA_INDEX_NAME)
      .setQuery(jsonSearch).execute().actionGet();
  }

  protected static MediaFile convertFromHit(ElasticSearchClient client,SearchHit hit) throws RuntimeException {
    String hitSource = hit.getSourceAsString();
    MediaFile mediaFile = null;
    try {
      mediaFile = client.getMapper().readValue(hitSource,MediaFile.class);
    }
    catch (IOException e) {
      throw new RuntimeException("Error while reading MediaFile object from index. ",e);
    }
    return mediaFile;
  }

  protected static MediaFile convertFromHitAndCollect(ElasticSearchClient client,SearchHit hit,List<MediaFile> list) throws IOException {
    MediaFile mediaFile = convertFromHit(client,hit);
    list.add(mediaFile);
    return mediaFile;
  }

}
 
