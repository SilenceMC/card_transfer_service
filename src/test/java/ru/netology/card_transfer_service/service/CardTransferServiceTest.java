package ru.netology.card_transfer_service.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.card_transfer_service.domain.Account;
import ru.netology.card_transfer_service.domain.Card;
import ru.netology.card_transfer_service.domain.enums.Currency;
import ru.netology.card_transfer_service.domain.enums.OperationStatus;
import ru.netology.card_transfer_service.domain.Operation;
import ru.netology.card_transfer_service.dto.request.AmountDTO;
import ru.netology.card_transfer_service.dto.request.ConfirmOperationDTO;
import ru.netology.card_transfer_service.dto.request.TransferRequestDTO;
import ru.netology.card_transfer_service.exception.CardValidateException;
import ru.netology.card_transfer_service.exception.OperationException;
import ru.netology.card_transfer_service.repository.CardTransferRepository;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardTransferServiceTest {

    @Mock
    private CardTransferRepository repository;

    @InjectMocks
    private CardTransferService service;

    @Nested
    class IsRequestValidTest {
        @Test
        void isRequestValid_CardNotFound_Test() {
            TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
            when(repository.getCard("1234123412341234")).thenReturn(Optional.empty());

            assertThrows(CardValidateException.class,
                    () -> service.isRequestValid(transferRequestDTO.setCardFromNumber("1234123412341234")),
                    "Карта списания не найдена");
        }

        @Test
        void isRequestValid_ValidTillIncorrect_Test() {
            TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
            when(repository.getCard("1234123412341234"))
                    .thenReturn(Optional.of(new Card().setValidTill("12/24")));

            assertThrows(CardValidateException.class,
                    () -> service.isRequestValid(transferRequestDTO
                            .setCardFromNumber("1234123412341234")
                            .setCardFromValidTill("12/25")),
                    "Срок действия карты некорректный");
        }

        @Test
        void isRequestValid_CVVIncorrect_Test() {
            TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
            when(repository.getCard("1234123412341234"))
                    .thenReturn(Optional.of(new Card().setValidTill("12/24").setCvv("123")));

            assertThrows(CardValidateException.class,
                    () -> service.isRequestValid(transferRequestDTO
                            .setCardFromNumber("1234123412341234")
                            .setCardFromValidTill("12/24")
                            .setCardFromCVV("234")),
                    "CVV-код некорректный");
        }

        @Test
        void isRequestValid_CurrencyIncorrect_Test() {
            TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
            when(repository.getCard("1234123412341234"))
                    .thenReturn(Optional.of(new Card()
                            .setValidTill("12/24")
                            .setCvv("123")
                            .setAccountNumber("1234123412341234")));
            when(repository.getAccount("1234123412341234")).thenReturn(new Account()
                    .setCurrency(Currency.RUR));

            assertThrows(CardValidateException.class,
                    () -> service.isRequestValid(transferRequestDTO
                            .setCardFromNumber("1234123412341234")
                            .setCardFromValidTill("12/24")
                            .setCardFromCVV("123")
                            .setAmount(new AmountDTO()
                                    .setCurrency(Currency.EUR))),
                    "Валюта счета списания не совпадает с валютой операции");
        }

        @Test
        void isRequestValid_OK_Test() {
            TransferRequestDTO transferRequestDTO = new TransferRequestDTO();
            when(repository.getCard("1234123412341234"))
                    .thenReturn(Optional.of(new Card()
                            .setValidTill("12/24")
                            .setCvv("123")
                            .setAccountNumber("1234123412341234")));

            when(repository.getAccount("1234123412341234"))
                    .thenReturn(new Account()
                            .setCurrency(Currency.RUR));

            when(repository.getCard("4321432143214321"))
                    .thenReturn(Optional.of(new Card()
                            .setNumber("4321432143214321")));

            assertTrue(service.isRequestValid(transferRequestDTO
                    .setCardFromNumber("1234123412341234")
                    .setCardFromValidTill("12/24")
                    .setCardFromCVV("123")
                    .setAmount(new AmountDTO()
                            .setCurrency(Currency.RUR))
                    .setCardToNumber("4321432143214321")));
        }
    }

    @Nested
    class IsTransferPossibleTest {
        @Test
        void isTransferPossible_Fail_Test() {
            TransferRequestDTO transferRequestDTO = new TransferRequestDTO();

            when(repository.getCard("123")).thenReturn(Optional.ofNullable(new Card().setAccountNumber("123")));
            when(repository.getAccount("123")).thenReturn(new Account().setValue(500));

            assertThrows(CardValidateException.class,
                    () -> service.isTransferPossible(transferRequestDTO.setCardFromNumber("123")
                            .setAmount(new AmountDTO().setValue(1000))),
                    "Недостаточно средств на счете карты списания");
        }

        @Test
        void isTransferPossible_OK_Test() {
            TransferRequestDTO transferRequestDTO = new TransferRequestDTO();

            when(repository.getCard("1234123412341234")).thenReturn(Optional.ofNullable(new Card().setAccountNumber("1234123412341234")));
            when(repository.getAccount("1234123412341234")).thenReturn(new Account().setValue(2000));

            assertTrue(service.isTransferPossible(transferRequestDTO.setCardFromNumber("1234123412341234")
                    .setAmount(new AmountDTO().setValue(1000))));
        }
    }

    @Nested
    class CreateTransferOperationTest {
        @Test
        void createTransferOperation_OK_Test() throws IOException {
            TransferRequestDTO transferRequestDTO = new TransferRequestDTO()
                    .setCardFromNumber("1234123412341234")
                    .setCardFromValidTill("12/24")
                    .setCardFromCVV("123")
                    .setCardToNumber("4321432143214321")
                    .setAmount(new AmountDTO()
                            .setCurrency(Currency.RUR)
                            .setValue(1000));

            when(repository.getCard("1234123412341234"))
                    .thenReturn(Optional.ofNullable(new Card()
                            .setNumber("1234123412341234")
                            .setAccountNumber("1234123412341234")
                            .setValidTill("12/24")
                            .setCvv("123")));
            when(repository.getAccount("1234123412341234"))
                    .thenReturn(new Account()
                            .setCurrency(Currency.RUR)
                            .setValue(2000));

            when(repository.getCard("4321432143214321"))
                    .thenReturn(Optional.ofNullable(new Card()
                            .setNumber("4321432143214321")));

            service.createTransferOperation(transferRequestDTO);
            verify(repository, times(1)).createTransferOperation(transferRequestDTO);
        }
    }

    @Nested
    class ConfirmTransferOperationTest {
        @Test
        void confirmTransferOperation_OperationNotFound_Test() {
            ConfirmOperationDTO confirmOperationDTO = new ConfirmOperationDTO();

            when(repository.getOperation(UUID.fromString("3eff2b83-9ea4-43c0-9a64-b11765106b89")))
                    .thenReturn(Optional.empty());

            assertThrows(OperationException.class,
                    () -> service.confirmTransferOperation(confirmOperationDTO
                            .setOperationId(UUID.fromString("3eff2b83-9ea4-43c0-9a64-b11765106b89"))),
                    "Нет операции перевода с указанным operationId");
        }

        @Test
        void confirmTransferOperation_DeclineOperation_Test() {
            ConfirmOperationDTO confirmOperationDTO = new ConfirmOperationDTO();

            when(repository.getOperation(UUID.fromString("3eff2b83-9ea4-43c0-9a64-b11765106b89")))
                    .thenReturn(Optional.of(new Operation()
                            .setOperationId(UUID.fromString("3eff2b83-9ea4-43c0-9a64-b11765106b89"))
                            .setStatus(OperationStatus.DECLINED)));

            assertThrows(OperationException.class,
                    () -> service.confirmTransferOperation(confirmOperationDTO
                            .setOperationId(UUID.fromString("3eff2b83-9ea4-43c0-9a64-b11765106b89"))),
                    "Операция с указанным operationId была отклонена");
        }

        @Test
        void confirmTransferOperation_ConfirmCodeIncorrect_Test() {
            ConfirmOperationDTO confirmOperationDTO = new ConfirmOperationDTO();

            when(repository.getOperation(UUID.fromString("3eff2b83-9ea4-43c0-9a64-b11765106b89")))
                    .thenReturn(Optional.of(new Operation()
                            .setOperationId(UUID.fromString("3eff2b83-9ea4-43c0-9a64-b11765106b89"))
                            .setStatus(OperationStatus.NEED_CONFIRM)
                            .setConfirmCode("0000")));

            assertThrows(OperationException.class,
                    () -> service.confirmTransferOperation(confirmOperationDTO
                            .setOperationId(UUID.fromString("3eff2b83-9ea4-43c0-9a64-b11765106b89"))
                            .setCode("1111")),
                    "Некорректный код подтверждения. Операция отклонена");
        }

        @Test
        void confirmTransferOperation_OK_Test() throws IOException {
            ConfirmOperationDTO confirmOperationDTO = new ConfirmOperationDTO();

            when(repository.getOperation(UUID.fromString("3eff2b83-9ea4-43c0-9a64-b11765106b89")))
                    .thenReturn(Optional.of(new Operation()
                            .setOperationId(UUID.fromString("3eff2b83-9ea4-43c0-9a64-b11765106b89"))
                            .setStatus(OperationStatus.NEED_CONFIRM)
                            .setConfirmCode("0000")));

            service.confirmTransferOperation(confirmOperationDTO
                    .setOperationId(UUID.fromString("3eff2b83-9ea4-43c0-9a64-b11765106b89"))
                    .setCode("0000"));

            verify(repository, times(1)).confirmTransferOperation(confirmOperationDTO);
        }
    }
}