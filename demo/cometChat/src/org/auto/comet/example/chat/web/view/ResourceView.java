package org.auto.comet.example.chat.web.view;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.*;

public class ResourceView implements View {
	private String APPLICATION_JSON = "application/json";
	private String APPLICATION_XML = "application/xml";
	private String DEFAULT_CONTENT_TYPE = APPLICATION_JSON;

	private Map<String, ResourceSerializer> resourceSerializerMap = new HashMap();

	{
		resourceSerializerMap.put(APPLICATION_JSON, new JsonSerializer());
		resourceSerializerMap.put(APPLICATION_XML, new XmlSerializer());
	}

	public String getContentType() {
		return "application";
	}

	private static List<String> getAccepts(HttpServletRequest request) {
		String acceptString = request.getHeader("Accept");
		if (StringUtils.isBlank(acceptString)) {
			return Collections.emptyList();
		}
		String[] acceptStrings = acceptString.split(",");
		List result = new LinkedList();
		for (String accept : acceptStrings) {
			accept = StringUtils.trim(accept);
			if (StringUtils.isNotBlank(accept)) {
				result.add(accept);
			}
		}
		return result;
	}

	private String getContentTyp(List<String> accepts) {
		for (String accept : accepts) {
			ResourceSerializer serializer = resourceSerializerMap.get(accept);
			if (resourceSerializerMap.containsKey(accept)) {
				return accept;
			}
		}
		return DEFAULT_CONTENT_TYPE;
	}

	private String getContentTyp(HttpServletRequest request) {
		List<String> accepts = getAccepts(request);
		return getContentTyp(accepts);
	}

	@SuppressWarnings("rawtypes")
	public void render(Map model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String contentTyp = getContentTyp(request);
		response.setContentType(contentTyp);
		ResourceSerializer serializer = resourceSerializerMap.get(contentTyp);

		if (null == model) {
			return;
		}
		Serializable jsonData = (Serializable) model
				.get(ResourceModelAndView.RESOURCE_DATA_ATTRIBUTE_NAME);
		if (null == jsonData)
			return;
		Object data = serializer.serialization(jsonData);
		if (null == data) {
			return;
		}
		if (data instanceof String) {
			Writer writer = response.getWriter();
			writer.write((String) data);
			writer.flush();
			return;
		}
		if (data instanceof byte[]) {
			OutputStream out = response.getOutputStream();
			out.write((byte[]) data);
			out.flush();
			return;
		}
		throw new IllegalStateException("Can't write data [" + data.getClass()
				+ "]");

	}

}
