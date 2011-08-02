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

	/**
	 * 请求头的Accept解析
	 */
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

	/**
	 * 返回第一种识别的数据类型，没有匹配则返回默认类型
	 */
	private String getContentTyp(List<String> accepts) {
		for (String accept : accepts) {
			ResourceSerializer serializer = resourceSerializerMap.get(accept);
			if (resourceSerializerMap.containsKey(accept)) {
				return accept;
			}
		}
		return DEFAULT_CONTENT_TYPE;
	}

	/**
	 * 获得请求的数据类型
	 */
	private String getContentTyp(HttpServletRequest request) {
		List<String> accepts = getAccepts(request);
		return getContentTyp(accepts);
	}

	public void render(Map model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String contentTyp = getContentTyp(request);
		response.setContentType(contentTyp);
		ResourceSerializer serializer = resourceSerializerMap.get(contentTyp);

		if (null == model) {
			return;
		}
		Object resource = model
				.get(ResourceModelAndView.RESOURCE_DATA_ATTRIBUTE_NAME);
		if (null == resource)
			return;
		Object data = serializer.serialization(resource);
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
