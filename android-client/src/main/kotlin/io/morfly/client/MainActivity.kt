package io.morfly.client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.morfly.client.graphql.GraphQLCommentsScreen
import io.morfly.client.graphql.GraphQlPostsScreen
import io.morfly.client.grpc.GRPCCommentsScreen
import io.morfly.client.grpc.GRPCPostsScreen
import io.morfly.client.rest.RESTCommentsScreen
import io.morfly.client.rest.RESTPostsScreen
import io.morfly.client.ui.MenuScreen
import io.morfly.client.ui.theme.AndroidClientTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidClientTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()

                    NavHost(navController, startDestination = "menu") {
                        composable("menu") {
                            MenuScreen(navController)
                        }
                        composable("rest/posts") {
                            RESTPostsScreen(navController)
                        }
                        composable("rest/comments/{postId}") {
                            RESTCommentsScreen(it.arguments?.getString("postId")!!)
                        }
                        composable("graphql/posts") {
                            GraphQlPostsScreen(navController)
                        }
                        composable("graphql/comments/{postId}") {
                            GraphQLCommentsScreen(it.arguments?.getString("postId")!!)
                        }
                        composable("grpc/posts") {
                            GRPCPostsScreen(navController)
                        }
                        composable("grpc/comments/{postId}") {
                            GRPCCommentsScreen(it.arguments?.getString("postId")!!)
                        }
                    }
                }
            }
        }
    }
}