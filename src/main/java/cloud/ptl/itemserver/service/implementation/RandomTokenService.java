package cloud.ptl.itemserver.service.implementation;

import cloud.ptl.itemserver.BeanInjector;
import cloud.ptl.itemserver.controllers.RandomTokenController;
import cloud.ptl.itemserver.error.exception.missing.ObjectNotFound;
import cloud.ptl.itemserver.persistence.dao.authentication.RandomTokenDAO;
import cloud.ptl.itemserver.persistence.dao.authentication.UserDAO;
import cloud.ptl.itemserver.persistence.dao.authorization.AclPermission;
import cloud.ptl.itemserver.persistence.repositories.authorization.RandomTokenRepository;
import cloud.ptl.itemserver.service.abstract2.AbstractDAOService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Optional;

@Service
public class RandomTokenService extends AbstractDAOService<RandomTokenDAO> {

    @Autowired
    private UserService userService;

    @Autowired
    private RandomTokenRepository randomTokenRepository;

    private final Logger logger = LoggerFactory.getLogger(RandomTokenService.class);

    public static RandomTokenDAO createToken(RandomTokenDAO.Type type, UserDAO userDAO){
        RandomTokenRepository randomTokenRepository =
                (RandomTokenRepository) BeanInjector.getBean(RandomTokenRepository.class);
        RandomTokenDAO randomTokenDAO = RandomTokenDAO.builder()
                .expiration(LocalDate.now().plus(Period.ofDays(2)))
                .owner(userDAO)
                .type(type)
                .token(
                        DigestUtils.sha512Hex(
                                LocalDateTime.now().minusYears(213).toString()
                        )
                )
                .build();
        randomTokenRepository.save(randomTokenDAO);
        return randomTokenDAO;
    }

    @Override
    public List<RandomTokenDAO> findAll(Pageable pageable, AclPermission permission) {
        this.logger.info("Searching all random tokens");
        this.logger.debug("pageable: " + pageable.toString());
        return this.randomTokenRepository.findRandomTokenDAOSByOwner(
                this.userService.getLoggedInUserDAO(),
                pageable
        );
    }

    @Override
    public RandomTokenDAO findById(Long id) throws ObjectNotFound {
        this.logger.info("Searching random token by id");
        this.logger.debug("id: " + id.toString());
        Optional<RandomTokenDAO> randomTokenDAO =
                this.randomTokenRepository.findById(id);
        if(randomTokenDAO.isEmpty()){
            this.logger.debug("Random token does not exist");
            throw new ObjectNotFound(
                    RandomTokenDAO.class.getCanonicalName(),
                    WebMvcLinkBuilder.linkTo(RandomTokenController.class).withSelfRel()
            );
        }
        return randomTokenDAO.get();
    }

    @Transactional
    public void delete(String token){
        this.logger.info("Deleting random token");
        this.randomTokenRepository.deleteRandomTokenDAOByToken(token);
    }

    public void delete(RandomTokenDAO randomTokenDAO){
        this.logger.info("Deleting random token");
        this.randomTokenRepository.delete(randomTokenDAO);
    }

    @Override
    public Boolean hasAccess(RandomTokenDAO item, AclPermission permission) {
        this.logger.info("Checking if user has access to token");
        this.logger.debug("token: " + item.toString());
        UserDAO loggedInUser = this.userService.getLoggedInUserDAO();
        this.logger.debug("userDAO: " + loggedInUser.toString());
        return item.getOwner().equals(loggedInUser);
    }

    public Boolean isValid(String code, UserDAO userDAO){
        this.logger.info("Checking if token is valid by code");
        this.logger.debug("code: " + code);
        Optional<RandomTokenDAO> randomTokenDAO =
                this.randomTokenRepository.findRandomTokenDAOByToken(code);
        if(randomTokenDAO.isEmpty()){
            this.logger.debug("Random token with given code does not exist");
            return false;
        }
        else{
            return this.isValid(randomTokenDAO.get(), userDAO);
        }
    }

    public Boolean isValid(RandomTokenDAO randomTokenDAO, UserDAO userDAO){
        this.logger.info("Checking if token is valid by random token dao");
        this.logger.debug("random token: " + randomTokenDAO.toString());
        if(randomTokenDAO.getExpiration().isBefore(LocalDate.now())){
            this.logger.debug("Token expired");
            return false;
        }
        Boolean userOwnsToken = randomTokenDAO.getOwner().equals(userDAO);
        if(userOwnsToken){
            this.logger.debug("user owns token");
            return true;
        }
        this.logger.debug("user does not own token");
        return false;
    }

    @Override
    public Boolean checkIfExists(Long id) throws ObjectNotFound {
        this.logger.info("checking if token exist");
        this.logger.debug("token id: " + id);
        return this.randomTokenRepository.existsById(id);
    }
}
