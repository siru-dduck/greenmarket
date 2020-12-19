package com.hanium.product.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public class ProductInterestDaoTest {

    @Autowired
    private IProductInterestDao dao;

    @Test
    public void countTest(){
        Assertions.assertThat(dao.count(11))
                .as("상품관심수")
                .isEqualTo(3);
    }

    @Test
    public void createTest(){
        int interestedCount = 0;
        interestedCount += dao.create(11,2);
        interestedCount += dao.create(11,3);
        interestedCount += dao.create(11,4);

        Assertions.assertThat(interestedCount).isEqualTo(3);
    }

    @Test
    public void deleteTest() {
        int deletedCount = 0;
        deletedCount += dao.delete(11,2);
        deletedCount += dao.delete(11,3);
        deletedCount += dao.delete(11,4);
        Assertions.assertThat(deletedCount).isEqualTo(0);
    }
}
