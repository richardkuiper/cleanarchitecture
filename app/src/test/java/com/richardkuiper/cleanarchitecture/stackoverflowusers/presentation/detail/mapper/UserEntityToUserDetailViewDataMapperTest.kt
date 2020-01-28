import com.richardkuiper.cleanarchitecture.stackoverflowusers.domain.model.UserEntity
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.detail.mapper.UserEntityToUserDetailViewDataMapper
import com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation.detail.model.UserDetailViewData
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.*
import org.mockito.junit.*

@RunWith(MockitoJUnitRunner::class)
class UserEntityToUserDetailViewDataMapperTest {

    @Test
    fun testUserEntityIsMappedToUserDetailViewData() {
        assertEquals(
                UserEntityToUserDetailViewDataMapper().map(userEntity()),
                expectedUserDetailViewData()
        )
    }

    private fun userEntity() =
            UserEntity(
                    accountId = 1,
                    age = 21,
                    reputation = 9,
                    creationDate = 10,
                    userType = "moderator",
                    location = "London",
                    profileImage = "some profile image url",
                    displayName = "Uncle Bob"
            )

    private fun expectedUserDetailViewData() =
            UserDetailViewData(
                    name = "Uncle Bob",
                    reputation = 9,
                    creationDate = 10,
                    userType = "moderator",
                    location = "London",
                    profileImage = "some profile image url"
            )
}