package ru.netology.card_transfer_service.service;

import org.junit.jupiter.api.BeforeAll;
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
import ru.netology.card_transfer_service.repository.CardTransferRepositoryImpl;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardTransferServiceTest {

    @Mock
    private CardTransferRepositoryImpl repository;

    @InjectMocks
    private CardTransferServiceImpl service;

    private static TransferRequestDTO transferRequestDTO;
    private static AmountDTO amount;
    private static ConfirmOperationDTO confirmOperationDTO;

    @BeforeAll
    static void init() {
        amount = new AmountDTO(1000, Currency.RUR);
        transferRequestDTO = new TransferRequestDTO(
                "1234123412341234",
                "12/24",
                "123",
                "4321432143214321",
                amount

        );
        confirmOperationDTO = new ConfirmOperationDTO(
                UUID.fromString("3eff2b83-9ea4-43c0-9a64-b11765106b89"),
                "0000"
        );
    }

    @Nested
    class IsRequestValidTest {

        @Test
        void isRequestValid_CardNotFound_Test() {
//            AmountDTO amount = new AmountDTO(2000, Currency.RUR);
//            TransferRequestDTO transferRequestDTO = new TransferRequestDTO(
//                    "1234123412341234",
//                    "12/24",
//                    "123",
//                    "1234123412341234",
//                    amount
//
//            );
            when(repository.getCard("1234123412341234")).thenReturn(Optional.empty());

            assertThrows(CardValidateException.class,
                    () -> service.isRequestValid(transferRequestDTO),
                    "Карта списания не найдена");
        }

        @Test
        void isRequestValid_ValidTillIncorrect_Test() {
            when(repository.getCard("1234123412341234"))
                    .thenReturn(Optional.of(new Card().setValidTill("12/25")));

            assertThrows(CardValidateException.class,
                    () -> service.isRequestValid(transferRequestDTO),
                    "Срок действия карты некорректный");
        }

        @Test
        void isRequestValid_CVVIncorrect_Test() {
            when(repository.getCard("1234123412341234"))
                    .thenReturn(Optional.of(new Card().setValidTill("12/24").setCvv("234")));

            assertThrows(CardValidateException.class,
                    () -> service.isRequestValid(transferRequestDTO),
                    "CVV-код некорректный");
        }

        @Test
        void isRequestValid_CurrencyIncorrect_Test() {
            when(repository.getCard("1234123412341234"))
                    .thenReturn(Optional.of(new Card()
                            .setValidTill("12/24")
                            .setCvv("123")
                            .setAccountNumber("1234123412341234")));
            when(repository.getAccount("1234123412341234")).thenReturn(new Account()
                    .setCurrency(Currency.EUR));

            assertThrows(CardValidateException.class,
                    () -> service.isRequestValid(transferRequestDTO),
                    "Валюта счета списания не совпадает с валютой операции");
        }

        @Test
        void isRequestValid_OK_Test() {
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

            assertTrue(service.isRequestValid(transferRequestDTO));
        }
    }

    @Nested
    class IsTransferPossibleTest {
        @Test
        void isTransferPossible_Fail_Test() {
            when(repository.getCard("1234123412341234"))
                    .thenReturn(Optional.ofNullable(new Card().setAccountNumber("123")));
            when(repository.getAccount("123")).thenReturn(new Account().setValue(100));

            assertThrows(CardValidateException.class,
                    () -> service.isTransferPossible(transferRequestDTO),
                    "Недостаточно средств на счете карты списания");
        }

        @Test
        void isTransferPossible_OK_Test() {
            when(repository.getCard("1234123412341234")).thenReturn(Optional.ofNullable(new Card().setAccountNumber("1234123412341234")));
            when(repository.getAccount("1234123412341234")).thenReturn(new Account().setValue(5000));

            assertTrue(service.isTransferPossible(transferRequestDTO));
        }
    }

    @Nested
    class CreateTransferOperationTest {
        @Test
        void createTransferOperation_OK_Test() throws IOException {
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
            when(repository.getOperation(UUID.fromString("3eff2b83-9ea4-43c0-9a64-b11765106b89")))
                    .thenReturn(Optional.empty());

            assertThrows(OperationException.class,
                    () -> service.confirmTransferOperation(confirmOperationDTO),
                    "Нет операции перевода с указанным operationId");
        }

        @Test
        void confirmTransferOperation_DeclineOperation_Test() {
            when(repository.getOperation(UUID.fromString("3eff2b83-9ea4-43c0-9a64-b11765106b89")))
                    .thenReturn(Optional.of(new Operation()
                            .setOperationId(UUID.fromString("3eff2b83-9ea4-43c0-9a64-b11765106b89"))
                            .setStatus(OperationStatus.DECLINED)));

            assertThrows(OperationException.class,
                    () -> service.confirmTransferOperation(confirmOperationDTO),
                    "Операция с указанным operationId была отклонена");
        }

        @Test
        void confirmTransferOperation_ConfirmCodeIncorrect_Test() {
            when(repository.getOperation(UUID.fromString("3eff2b83-9ea4-43c0-9a64-b11765106b89")))
                    .thenReturn(Optional.of(new Operation()
                            .setOperationId(UUID.fromString("3eff2b83-9ea4-43c0-9a64-b11765106b89"))
                            .setStatus(OperationStatus.NEED_CONFIRM)
                            .setConfirmCode("1111")));

            assertThrows(OperationException.class,
                    () -> service.confirmTransferOperation(confirmOperationDTO),
                    "Некорректный код подтверждения. Операция отклонена");
        }

        @Test
        void confirmTransferOperation_OK_Test() throws IOException {
            when(repository.getOperation(UUID.fromString("3eff2b83-9ea4-43c0-9a64-b11765106b89")))
                    .thenReturn(Optional.of(new Operation()
                            .setOperationId(UUID.fromString("3eff2b83-9ea4-43c0-9a64-b11765106b89"))
                            .setStatus(OperationStatus.NEED_CONFIRM)
                            .setConfirmCode("0000")));

            service.confirmTransferOperation(confirmOperationDTO);

            verify(repository, times(1)).confirmTransferOperation(confirmOperationDTO);
        }
    }
}