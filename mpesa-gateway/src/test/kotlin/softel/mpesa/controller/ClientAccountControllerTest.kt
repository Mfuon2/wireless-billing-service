// class PagedControllerTest {

//     @MockBean
//     private MovieCharacterRepository characterRepository;
  
//     @Autowired
//     private MockMvc mockMvc;
  
//     @Test
//     void evaluatesPageableParameter() throws Exception {
  
//       mockMvc.perform(get("/characters/page")
//           .param("page", "5")
//           .param("size", "10")
//           .param("sort", "id,desc")   // <-- no space after comma!
//           .param("sort", "name,asc")) // <-- no space after comma!
//           .andExpect(status().isOk());
  
//       ArgumentCaptor<Pageable> pageableCaptor = 
//           ArgumentCaptor.forClass(Pageable.class);
//       verify(characterRepository).findAllPage(pageableCaptor.capture());
//       PageRequest pageable = (PageRequest) pageableCaptor.getValue();
  
//       assertThat(pageable).hasPageNumber(5);
//       assertThat(pageable).hasPageSize(10);
//       assertThat(pageable).hasSort("name", Sort.Direction.ASC);
//       assertThat(pageable).hasSort("id", Sort.Direction.DESC);
//     }
//   }