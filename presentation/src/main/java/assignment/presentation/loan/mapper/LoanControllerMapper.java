package assignment.presentation.loan.mapper;

import static assignment.application.service.loan.dto.response.LoanResponseDto.*;
import static assignment.presentation.loan.vo.response.LoanResponse.*;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LoanControllerMapper {
	CheckLoanResponse toCheckLoanResponse(CheckLoanResponseDto responseDto);
}
