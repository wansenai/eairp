fn main() {
    println!("Hello, world!");
}

fn check_tf() {
    let x = true;
    let y: bool = false;
    println!("x is {}", x);
    println!("y is {}", y);

    let values: Vec<u64> = (0..100000).collect();
    let t = Tensor::new(&[2, 50000]).with_values(&values).unwrap();

    dbg!(t);
}