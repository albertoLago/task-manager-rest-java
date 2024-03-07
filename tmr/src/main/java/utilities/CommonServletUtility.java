package utilities;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import dtos.generic.GenericErrorResponseDTO;
import dtos.generic.GenericResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CommonServletUtility {

    /**
     * private constructor to forbid instantiation
     */
    private CommonServletUtility() {
    }

    /**
     * method to map request body data to a corresponding instance of a POJO class
     * 
     * @param <T>         - type of the target mapped object
     * @param request     - instance of HttpServletRequest containing the
     *                    request-body data
     * @param targetClass - class of the target object which will be created and
     *                    populated
     * @return an instance of the target class with the required data mapped from
     *         the request-body
     * @throws JsonSyntaxException - if json is not a valid representation for an
     *                             object of type typeOf T
     * @throws JsonIOException     - if there was a problem reading from the Reader
     * @throws IOException         - if an input or output exception occurred
     */
    public static <T> T mapRequestBodyToObject(HttpServletRequest request, Class<T> targetClass)
            throws JsonSyntaxException, JsonIOException, IOException {
        // retrieves the body data from the instance of HttpServletRequest and map that
        // JSON data to an instance of the given target class
        return new Gson().fromJson(request.getReader(), targetClass);
    }

    /**
     * method to build error response
     * 
     * @param response        - instance of HttpServletResponse
     * @param errorStatusCode - status code to be set for the error response
     * @param e               - instance of the type throwable
     */
    public static void buildErrorResponse(HttpServletResponse response, int errorStatusCode, Throwable e) {
        // set response metadata
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorStatusCode);

        try {
            // add error information data to the error response if the given error object is
            // not null
            if (e != null) {
                response.getWriter()
                        .write(new Gson().toJson(new GenericErrorResponseDTO(e.getLocalizedMessage())));
            }
        } catch (IOException exception) {
            // TODO: log error message to console
        }
    }

    /**
     * method to build success response
     * 
     * @param <T>                - type of the response data object to be added with
     *                           the instance of HttpServletResponse
     * @param response           - instance of HttpServletResponse
     * @param responseStatusCode - status code to be set for the response
     * @param responseData       - response data to be added with the instance of
     *                           the HttpServletResponse
     */
    public static <T> void buildSuccessResponse(HttpServletResponse response, int responseStatusCode, T responseData) {
        // set response metadata
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(responseStatusCode);

        try {
            // add response data to response if the given response data is not null
            if (responseData != null) {
                response.getWriter().write(new Gson().toJson(new GenericResponseDTO<T>(responseData)));
            }
        } catch (IOException e) {
            // TODO: log error message to console
        }
    }
}