package br.com.application.wallet.models.dto.form;

import br.com.application.wallet.models.Wallet;
import br.com.application.wallet.models.api.ErrorMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WalletForm implements Serializable {
    private static final long serialVersionUID = 762849513409120692L;

    @NotNull(message = ErrorMessage.NOT_NULL_FIELD)
    @NotEmpty(message = ErrorMessage.NOT_EMPTY_FIELD)
    @JsonProperty("wallet_description")
    private String description;

    @NotNull(message = ErrorMessage.NOT_NULL_FIELD)
    @NotEmpty(message = ErrorMessage.NOT_EMPTY_FIELD)
    @Positive(message = ErrorMessage.INVALID_FIELD)
    @JsonProperty("wallet_balance")
    private BigDecimal balance;

}
