package org.hopto.fjavierjp.petregistry.request;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class QueryParameters
{
    private Pageable pageable;
    private Map<String, String> filters;

    private QueryParameters() {}
    public QueryParameters(
        String sort,
        String direction,
        @Min(0) Integer page,
        @Min(1) Integer size,
        @Min(1) Integer limit,
        Map<String, String> filters
    )
    {
        Sort sortObj = Sort.unsorted();
        this.pageable = null;
        if (sort != null && direction != null)
        {
            Sort.Direction sortDirection = Sort.Direction.fromOptionalString(direction).orElse(Sort.Direction.DESC);
            sortObj = Sort.by(sortDirection, sort);

            if (page != null && size != null)
            {
                this.pageable = PageRequest.of(page, size, sortObj);
            }
            else
            {
                if (limit != null)
                {
                    this.pageable = PageRequest.of(0, limit, sortObj);
                }
                else
                {
                    this.pageable = PageRequest.of(0, Integer.MAX_VALUE, sortObj);
                }
            }
        }
        else
        {
            if (page != null && size != null)
            {
                this.pageable = PageRequest.of(page, size);
            }
            else
            {
                if (limit != null)
                {
                    this.pageable = PageRequest.of(0, limit);
                }
                else
                {
                    this.pageable = PageRequest.of(0, Integer.MAX_VALUE);
                }
            }
        }
        
        this.filters = filters;
    }
}
