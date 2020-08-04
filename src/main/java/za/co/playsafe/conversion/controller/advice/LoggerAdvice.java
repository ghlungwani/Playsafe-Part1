package za.co.playsafe.conversion.controller.advice;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import za.co.playsafe.conversion.util.JsonUtility;

@Aspect
@Component
public class LoggerAdvice {

	private static final String REQUEST_URI_IDENTIFIER = "requestUri";
	private static final String REMOTE_HOST_IDENTIFIER = "remoteHost";
	private static final Set<String> HEADERS_TO_LOG = Collections.unmodifiableSet(new HashSet<>(Arrays.asList()));

	@Value("${logging.log_request}")
	private boolean logRequest;

	@Value("${logging.log_response}")
	private boolean logResponse;

	@Around("within(@org.springframework.stereotype.Service *)")
	public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
		Logger logger = getLogger(joinPoint);

		String methodName = joinPoint.getSignature().getName();

		logMethodDetails(joinPoint.getArgs(), methodName, logger);

		return timeAndExecute(joinPoint, logger, methodName);
	}

	private void logMethodDetails(Object[] args, String methodName, Logger logger) {
		String log = convertResponseToString(args);

		if (logRequest) {
			logger.info("Executing method [{}] with request [{}]", methodName, log);
		} else {
			logger.info("Executing method [{}]", methodName, log);
		}

	}

	private Logger getLogger(ProceedingJoinPoint joinPoint) {
		String className = joinPoint.getSignature().getDeclaringTypeName();
		return LoggerFactory.getLogger(className);
	}

	@Around("within(@org.springframework.web.bind.annotation.RestController *)")
	public Object logHttpRequest(ProceedingJoinPoint joinPoint) throws Throwable {
		Logger logger = getLogger(joinPoint);

		String methodName = joinPoint.getSignature().getName();

		logHttpRequest(methodName, logger);

		return timeAndExecute(joinPoint, logger, methodName);
	}

	private Object timeAndExecute(ProceedingJoinPoint joinPoint, Logger logger, String methodName) throws Throwable {
		try {
			StopWatch stopWatch = new StopWatch();
			stopWatch.start();

			Object result = joinPoint.proceed();

			stopWatch.stop();

			if (logResponse) {
				String jsonResponse = convertResponseToString(result);
				logger.info("Method [{}] executed within [{}] milliseconds with response [{}]", methodName,
						stopWatch.getTotalTimeMillis(), jsonResponse);
			} else {
				logger.info("Method [{}] executed within [{}] milliseconds", methodName,
						stopWatch.getTotalTimeMillis());
			}

			return result;
		} catch (Exception ex) {
			logger.error("Exception was raised while trying to execute method " + methodName, ex);
			throw ex;
		}
	}

	private void logHttpRequest(String methodName, Logger logger) throws IOException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();

		Map<String, String> loggingObjects = buildMapOfObjectsToLogFromHttpRequest(request);

		if (logRequest) {
			String log = convertResponseToString(loggingObjects);
			logger.info("Executing method [{}] with request [{}]", methodName, log);
		} else {
			logger.info("Executing method [{}]", methodName);
		}
	}

	private Map<String, String> buildMapOfObjectsToLogFromHttpRequest(HttpServletRequest request) throws IOException {
		Map<String, String> loggingObjects = new HashMap<>();

		HEADERS_TO_LOG.stream().filter(header -> request.getHeader(header) != null)
				.forEach(header -> loggingObjects.put(header, request.getHeader(header)));

		loggingObjects.put(REQUEST_URI_IDENTIFIER, request.getRequestURI());
		loggingObjects.put(REMOTE_HOST_IDENTIFIER, request.getRemoteHost());
		return loggingObjects;
	}

	private String convertResponseToString(Object result) {
		try {
			return JsonUtility.convertObjectToString(result);
		} catch (Exception e) {
			return result == null ? StringUtils.EMPTY : result.toString();
		}
	}
}